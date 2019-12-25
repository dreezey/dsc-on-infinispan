/*
 * Copyright 2019 IS4U NV. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.cronos.services;

import be.cronos.DsessConstants;
import be.cronos.model.*;
import be.cronos.model.ispn.Replica;
import be.cronos.model.ispn.Session;
import be.cronos.model.ispn.SessionData;
import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.*;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class DSessService {
    private static final Logger LOG = Logger.getLogger(DSessService.class.getName());

    // Cache should first be added in Infinispan, otherwise you get "org.infinispan.server.hotrod.CacheNotFoundException"
    @Inject
    @Remote("replicas")
    RemoteCache<String, Replica> replicasRemoteCache;
    @Inject
    @Remote("sessions")
    RemoteCache<String, Session> sessionsRemoteCache;

    public JoinReplicaSetResponse joinReplicaSet(JoinReplicaSetRequest joinReplicaSetRequest) {
        Replica replica = attemptCacheReplica(joinReplicaSetRequest);

        return new JoinReplicaSetResponse();
    }

    public ReplicaShutdownResponse shutdownReplica(ReplicaShutdownRequest replicaShutdownRequest) {
        Replica removedReplica = shutdownReplica(replicaShutdownRequest.getReplica());

        if (removedReplica == null) {
            LOG.finest("replica was not in cache anyway");
        } else {
            LOG.finest("replica removed from cache.");
        }
        return new ReplicaShutdownResponse();
    }

    public GetRealmNameResponse getRealmName(GetRealmNameRequest getRealmNameRequest) {
        return new GetRealmNameResponse();
    }

    public CreateSessionResponse createSession(CreateSessionRequest createSessionRequest) {
        Session newSession = cacheSession(createSessionRequest);
        return new CreateSessionResponse();
    }

    public GetSessionResponse getSession(GetSessionRequest getSessionRequest) {
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
        return getSessionResponse;
    }

    public IdleTimeoutResponse idleTimeout(IdleTimeoutRequest idleTimeoutRequest) {
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
                        LOG.finest("Session already marked as inactive");
                        break;
                    }
                    LOG.finest("Marking as inactive");
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
            LOG.finest("no session found in cache");
        }

        return new IdleTimeoutResponse();
    }

    public TerminateSessionResponse terminateSession(TerminateSessionRequest terminateSessionRequest) {
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
        return new TerminateSessionResponse(terminateSessionReturn);
    }

    public ChangeSessionResponse changeSession(ChangeSessionRequest changeSessionRequest) {
        LOG.finest(changeSessionRequest.toString());
        if (changeSessionRequest.getData() == null) {
            LOG.warning("Updating a session with no new data, ignoring request...");
            return constructChangeSessionResponse(changeSessionRequest.getVersion(), false);
        }
        String sessionId = changeSessionRequest.getSessionId();
        MetadataValue<Session> cachedSessionWithMetadata = getSessionFromCacheWithMetadata(sessionId);

        ChangeSessionResponse changeSessionResponse;
        if (cachedSessionWithMetadata != null) {
            LOG.finest("Found a session in cache for key: " + sessionId);
            Session cachedSession = cachedSessionWithMetadata.getValue();
            if (cachedSession.getReplicaSet().equalsIgnoreCase(changeSessionRequest.getReplicaSet())) {
                // replica set matches
                Session updatedSession = Session.shallowCopy(cachedSession);
                // Update normal properties first
                updatedSession.setSessionLimit(changeSessionRequest.getSessionLimit());
                // And now we'll update the data
                for (SessionDataRequest sessionDataRequest : changeSessionRequest.getData()) {
                    int indexToModify = findIndexOfSessionAttribute(updatedSession.getSessionData(), sessionDataRequest.getDataClass());
                    if (indexToModify == -1) {
                        // Add the new Session Data to the list
                        LOG.finest("Adding new data: " + sessionDataRequest.toString());
                        updatedSession.getSessionData().add(sessionDataRequest);
                    } else {
                        // Update existing session data
                        LOG.finest("Modifying existing session data: " + sessionDataRequest.toString());
                        updatedSession.getSessionData().set(indexToModify, sessionDataRequest);
                    }
                }
                // And finally update the remote cache
                sessionsRemoteCache.replace(sessionId, updatedSession, cachedSessionWithMetadata.getLifespan(), TimeUnit.SECONDS);
                // Prepare the ChangeSessionResponse
                changeSessionResponse = constructChangeSessionResponse(changeSessionRequest.getVersion(), true);
            } else {
                changeSessionResponse = constructChangeSessionResponse(changeSessionRequest.getVersion(), false);
                LOG.warning("Attempted to change session with '" + sessionId + "', but requested replica set is different, ignoring.");
            }
        } else {
            LOG.finest("No associated session was found for key: " + sessionId);
            changeSessionResponse = constructChangeSessionResponse(changeSessionRequest.getVersion(), false);
        }

        return changeSessionResponse;
    }

    private int findIndexOfSessionAttribute(ArrayList<SessionData> sourceSessionData, String dataClass) {
        for (int i=0; i < sourceSessionData.size(); i++) {
            SessionData sessionData = sourceSessionData.get(i);
            if (sessionData.getDataClass().equalsIgnoreCase(dataClass)) {
                return i;
            }
        }
        return -1;
    }

    private ChangeSessionResponse constructChangeSessionResponse(int version, boolean updated) {
        ChangeSessionReturn changeSessionReturn;
        if (updated) {
            changeSessionReturn = new ChangeSessionReturn(version);
        } else {
            changeSessionReturn = new ChangeSessionReturn(version + 1);
        }
        return new ChangeSessionResponse(changeSessionReturn);
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
            LOG.finest("Replica is not in cache yet, caching...");
            Replica replica = new Replica(replicaName, joinReplicaSetRequest.getInstance(), joinReplicaSetRequest.getCapabilities(), joinReplicaSetRequest.getReplicaSet());
            return replicasRemoteCache.put(replicaName, replica);
        } else {
            LOG.finest("Replica was already cached, printing details ...");
            LOG.finest("replica = " + cachedReplica.toString());
            return cachedReplica;
        }
    }

    private Replica shutdownReplica(String replicaName) {
        return replicasRemoteCache
                .withFlags(Flag.FORCE_RETURN_VALUE)
                .remove(replicaName);
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

}
