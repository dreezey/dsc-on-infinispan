package be.cronos.services;

import be.cronos.DsessConstants;
import be.cronos.model.*;
import be.cronos.model.ispn.Replica;
import be.cronos.model.ispn.Session;
import be.cronos.model.ispn.SessionData;
import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.*;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

@ApplicationScoped
public class DSessService {
    private final String CN = this.getClass().getName();
    private static final Logger LOG = Logger.getLogger(DSessService.class.getName());

    // Cache should first be added in Infinispan, otherwise you get "org.infinispan.server.hotrod.CacheNotFoundException"
    @Inject
    @Remote("replicas")
    RemoteCache<String, Replica> replicasRemoteCache;
    @Inject
    @Remote("sessions")
    RemoteCache<String, Session> sessionsRemoteCache;

    public Response joinReplicaSet(JoinReplicaSetRequest joinReplicaSetRequest) {
        Replica replica = attemptCacheReplica(joinReplicaSetRequest);

        return Response
                .status(200)
                .entity(new JoinReplicaSetResponse())
                .build();
    }

    public Response shutdownReplica(ReplicaShutdownRequest replicaShutdownRequest) {
        Replica removedReplica = shutdownReplica(replicaShutdownRequest.getReplica());

        if (removedReplica == null) {
            LOG.info("replica was not in cache anyway");
        } else {
            LOG.info("replica removed from cache.");
        }
        return Response
                .status(200)
                .entity(new ReplicaShutdownResponse())
                .build();
    }

    public Response getRealmName(GetRealmNameRequest getRealmNameRequest) {
        return Response
                .status(200)
                .entity(new GetRealmNameResponse())
                .build();
    }

    public Response createSession(CreateSessionRequest createSessionRequest) {
        Session newSession = cacheSession(createSessionRequest);
        LOG.info("session info = " + newSession);
        return Response
                .status(200)
                .entity(new CreateSessionResponse())
                .build();
    }

    public Response getSession(GetSessionRequest getSessionRequest) {
        String sessionId = getSessionRequest.getSessionId();
        Session cachedSession = getSessionFromCache(sessionId);
        GetSessionResponse getSessionResponse;
        if (cachedSession == null) {
            LOG.finest("no cached session found with ID: " + sessionId);
            getSessionResponse = constructEmptyGetSessionResponse();
        } else {
            LOG.finest("Session found in cache: " + sessionId);
            if (getSessionRequest.getReplicaSet().equalsIgnoreCase(cachedSession.getReplicaSet())) {
                LOG.finest("Requested replicaset matches cached replicaset: " + cachedSession.getReplicaSet());
                LOG.finest("Returning valid session response.");
                ArrayList<GetSessionDataReturn> getSessionDataReturn = new ArrayList<>();
                cachedSession.getSessionData().forEach(sessionData -> {
                    getSessionDataReturn.add(new GetSessionDataReturn(
                            sessionData.getDataClass(),
                            sessionData.getValue(),
                            sessionData.getInstance(),
                            sessionData.getChangePolicy()
                    ));
                });
                GetSessionReturn getSessionReturn = new GetSessionReturn(
                        0,
                        getSessionDataReturn
                );
                getSessionResponse = new GetSessionResponse(getSessionReturn);
            } else {
                LOG.warning("Requested session ID was found, but the replica set does not match, returning empty response.");
                getSessionResponse = constructEmptyGetSessionResponse();
            }
        }
        return Response
                .status(200)
                .entity(getSessionResponse)
                .build();
    }

    public Response idleTimeout(IdleTimeoutRequest idleTimeoutRequest) {
        String sessionId = idleTimeoutRequest.getSessionId();
        MetadataValue<Session> cachedSessionWithMetadata = getSessionFromCacheWithMetadata(sessionId);

        if (cachedSessionWithMetadata != null) {
            // Get the session data
            Session cachedSession = cachedSessionWithMetadata.getValue();
            // Make a copy of the cached session
            Session updatedSession = Session.shallowCopy(cachedSession);
            boolean sessionUpdated = false;
            for (int i=0; i < cachedSession.getSessionData().size(); i++) {
                SessionData sessionData = cachedSession.getSessionData().get(i);
                if (sessionData.getDataClass().equalsIgnoreCase(DsessConstants.IS_INACTIVE_DATACLASS)) {
                    if (sessionData.getValue().equalsIgnoreCase("true")) {
                        LOG.info("Session already marked as inactive");
                        break;
                    }
                    LOG.info("Marking as inactive");
                    sessionData.setValue("true");
                    updatedSession.getSessionData().set(i, sessionData);
                    sessionUpdated = true;
                    break;
                }
            }

            if (sessionUpdated) {
                //TODO still resets the timer to the original value, may need to review how to properly handle lifetime
                sessionsRemoteCache
//                    .withFlags(Flag.FORCE_RETURN_VALUE) // don't force return value to prevent unnecessary (un)marshalling
                        .replace(
                                sessionId,
                                updatedSession,
                                cachedSessionWithMetadata.getLifespan(),
                                TimeUnit.SECONDS
                        );
            }
        } else {
            LOG.info("no session found in cache");
        }

        return Response
                .status(200)
                .entity(new IdleTimeoutResponse())
                .build();
    }

    public Response terminateSession(TerminateSessionRequest terminateSessionRequest) {
        // TODO when terminating a session, it should be added to a graveyard of some sort to let "getUpdates" know.
        LOG.finest("Incoming TerminateSessionRequest: " + terminateSessionRequest.toString());
        String sessionId = terminateSessionRequest.getSessionId();
        Session cachedSession = getSessionFromCache(sessionId);

        if (cachedSession != null) {
            LOG.finest("There's a session in cache, preparing to delete");
            if (cachedSession.getReplicaSet().equalsIgnoreCase(terminateSessionRequest.getReplicaSet())) {
                LOG.finest("Removing session: " + sessionId);
                sessionsRemoteCache.remove(sessionId);
            } else {
                LOG.warning("Termination of session with id '" + sessionId + "' failed because the replica sets do not match.");
            }
        }
        TerminateSessionReturn terminateSessionReturn = new TerminateSessionReturn(terminateSessionRequest.getVersion());
        TerminateSessionResponse terminateSessionResponse = new TerminateSessionResponse(terminateSessionReturn);
        return Response
                .status(200)
                .entity(terminateSessionResponse)
                .build();
    }

    private GetSessionResponse constructEmptyGetSessionResponse() {
        GetSessionReturn getSessionReturn = new GetSessionReturn(
                0,
                null
        );
        return new GetSessionResponse(getSessionReturn);
    }

    private Replica attemptCacheReplica(JoinReplicaSetRequest joinReplicaSetRequest) {
        String replicaName = joinReplicaSetRequest.getReplica();
        Replica cachedReplica = replicasRemoteCache.get(replicaName);
        if (cachedReplica == null) {
            LOG.info("Replica is not in cache yet, caching...");
            Replica replica = new Replica(replicaName, joinReplicaSetRequest.getInstance(), joinReplicaSetRequest.getCapabilities(), joinReplicaSetRequest.getReplicaSet());
            return replicasRemoteCache.put(replicaName, replica);
        } else {
            LOG.info("Replica was already cached, printing details ...");
            LOG.info("replica = " + cachedReplica.toString());
            return cachedReplica;
        }
    }

    private Replica shutdownReplica(String replicaName) {
        return replicasRemoteCache.remove(replicaName);
    }

    private Session cacheSession(CreateSessionRequest createSessionRequest) {
        String sessionId = createSessionRequest.getSessionId();
        LOG.finest("sessionid = " + sessionId);
        // Get the session inactivity timeout first
        AtomicReference<String> sessionInactivityTimeout = new AtomicReference<>();
        createSessionRequest.getData().forEach(sessionDataRequest -> {
            if (sessionDataRequest.getDataClass().equalsIgnoreCase(DsessConstants.INACTIVITY_TIMEOUT)) {
                sessionInactivityTimeout.set(sessionDataRequest.getValue());
            }
        });
        // Parse the session inactivity to a long
        long lifetime = 0L;
        try {
            lifetime = Long.parseLong(sessionInactivityTimeout.get().replaceFirst("0x", ""), 16);
        } catch (NumberFormatException ignored) {

        }
        LOG.finest("use session lifetime of " + lifetime);

        // Construct the Session Object to be stored in Infinispan
        Session cachedSession = new Session(
                createSessionRequest.getSessionId(),
                createSessionRequest.getReplicaSet(),
                createSessionRequest.getSessionLimit(),
                new ArrayList<>(createSessionRequest.getData())
        );

        if (lifetime == 0L) {
            return sessionsRemoteCache
//                    .withFlags(Flag.FORCE_RETURN_VALUE) //this will make sure a value is returned, at the cost of marshalling
                    .putIfAbsent(sessionId, cachedSession);
        } else {
            return sessionsRemoteCache
//                    .withFlags(Flag.FORCE_RETURN_VALUE) //this will make sure a value is returned, at the cost of marshalling
                    .putIfAbsent(sessionId, cachedSession, lifetime, TimeUnit.SECONDS);
        }
    }

    private Session getSessionFromCache(String sessionId) {
        return sessionsRemoteCache.get(sessionId);
    }

    private MetadataValue<Session> getSessionFromCacheWithMetadata(String sessionId) {
        return sessionsRemoteCache.getWithMetadata(sessionId);
    }

    private List<Replica> searchReplicasByReplicaSet(String replicaSet) {
        QueryFactory queryFactory = Search.getQueryFactory(replicasRemoteCache);
        Query query = queryFactory
                .from(Replica.class)
                .having("replicaSet").eq(replicaSet)
                .build();
        return query.list();
    }

}
