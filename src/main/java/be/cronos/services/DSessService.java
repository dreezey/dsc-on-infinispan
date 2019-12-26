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
import be.cronos.model.ispn.Replica;
import be.cronos.model.ispn.Session;
import be.cronos.model.ispn.SessionData;
import be.cronos.view.*;
import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.*;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
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

        return new JoinReplicaSetResponse(
                new JoinReplicaSetReturn(
                        DSCResultCode.OK.getResultCode(),
                        0,
                        0,
                        -1
                )
        );
    }

    public ReplicaShutdownResponse shutdownReplica(ReplicaShutdownRequest replicaShutdownRequest) {
        Replica removedReplica = shutdownReplica(replicaShutdownRequest.getReplica());

        if (removedReplica == null) {
            LOG.info("replica was not in cache anyway");
        } else {
            LOG.info("replica removed from cache.");
        }
        return new ReplicaShutdownResponse(
                DSCResultCode.OK.getResultCode()
        );
    }

    public GetRealmNameResponse getRealmName(GetRealmNameRequest getRealmNameRequest) {
        return new GetRealmNameResponse(
                new GetRealmNameReturn(
                        DSCResultCode.OK.getResultCode(),
                        DsessConstants.REALM_NAME
                )
        );
    }

    public CreateSessionResponse createSession(CreateSessionRequest createSessionRequest) {
        return cacheSession(createSessionRequest);
    }

    public GetSessionResponse getSession(GetSessionRequest getSessionRequest) {
        String sessionId = getSessionRequest.getSessionId();
        if (sessionId == null) {
            LOG.warning("No session ID or data.");
            return constructGetSessionResponse(
                    DSCResultCode.NOT_CHANGED,
                    0,
                    1,
                    null
            );
        }
        LOG.finest("Session ID = " + sessionId);
        // Now validate whether the replica set is valid
        Replica cachedReplica = getReplicaFromCache(getSessionRequest.getReplica());
        if (cachedReplica == null) {
            LOG.warning("cached replica is null.");
            return constructGetSessionResponse(
                    DSCResultCode.REPLICA_SET_NOT_FOUND,
                    0,
                    1,
                    null
            );
        }
        if (!cachedReplica.getReplicaSet().equalsIgnoreCase(getSessionRequest.getReplicaSet())) {
            LOG.warning("replica sets don't match.");
            return constructGetSessionResponse(
                    DSCResultCode.REPLICA_SET_NOT_FOUND,
                    0,
                    1,
                    null
            );
        }

        Session cachedSession = getSessionFromCache(sessionId);
        GetSessionResponse getSessionResponse;
        if (cachedSession == null) {
            LOG.fine("no cached session found with ID: " + sessionId);
            return constructGetSessionResponse(
                    DSCResultCode.NOT_CHANGED,
                    0,
                    1,
                    null
            );
        } else {
            LOG.fine("Session found in cache: " + sessionId);
            ArrayList<GetSessionDataReturn> getSessionDataReturn = new ArrayList<>();
            cachedSession.getSessionData().forEach(sessionData -> {
                getSessionDataReturn.add(new GetSessionDataReturn(
                        sessionData.getDataClass(),
                        sessionData.getValue(),
                        sessionData.getInstance(),
                        sessionData.getChangePolicy()
                ));
            });
            return constructGetSessionResponse(
                    DSCResultCode.OK,
                    0,
                    1,
                    getSessionDataReturn
            );
        }
    }

    public IdleTimeoutResponse idleTimeout(IdleTimeoutRequest idleTimeoutRequest) {
        return new IdleTimeoutResponse(
                terminateSession(
                        idleTimeoutRequest.getSessionId(),
                        idleTimeoutRequest.getReplica(),
                        idleTimeoutRequest.getReplicaSet()
                ).getResultCode()
        );
    }

    public TerminateSessionResponse terminateSession(TerminateSessionRequest terminateSessionRequest) {
        return new TerminateSessionResponse(
                new TerminateSessionReturn(
                        terminateSession(
                                terminateSessionRequest.getSessionId(),
                                terminateSessionRequest.getReplica(),
                                terminateSessionRequest.getReplicaSet()
                        ).getResultCode(),
                        0,
                        1,
                        false
                )
        );
    }

    public ChangeSessionResponse changeSession(ChangeSessionRequest changeSessionRequest) {
        if (changeSessionRequest.getSessionId() == null || changeSessionRequest.getData() == null) {
            LOG.warning("Updating a session with no new data or missing session id, ignoring request...");
            return constructChangeSessionResponse(changeSessionRequest.getVersion(), false, DSCResultCode.NOT_CHANGED);
        }
        // Now validate whether the replica set is valid
        Replica cachedReplica = getReplicaFromCache(changeSessionRequest.getReplica());
        if (cachedReplica == null) {
            LOG.warning("cached replica is null.");
            return constructChangeSessionResponse(changeSessionRequest.getVersion(), false, DSCResultCode.REPLICA_SET_NOT_FOUND);
        }
        if (!cachedReplica.getReplicaSet().equalsIgnoreCase(changeSessionRequest.getReplicaSet())) {
            LOG.warning("replica sets don't match.");
            return constructChangeSessionResponse(changeSessionRequest.getVersion(), false, DSCResultCode.REPLICA_SET_NOT_FOUND);
        }
        String sessionId = changeSessionRequest.getSessionId();
        MetadataValue<Session> cachedSessionWithMetadata = getSessionFromCacheWithMetadata(sessionId);

        ChangeSessionResponse changeSessionResponse;
        if (cachedSessionWithMetadata == null) {
            LOG.warning("No associated session was found for key: " + sessionId);
            changeSessionResponse = constructChangeSessionResponse(changeSessionRequest.getVersion(), false, DSCResultCode.NOT_CHANGED);
        } else {
            LOG.finest("Found a session in cache for key: " + sessionId);
            Session cachedSession = cachedSessionWithMetadata.getValue();
            if (cachedSession == null) {
                LOG.severe("Session ID yielded data, but Session appears to be null, this should not occur.");
                changeSessionResponse = constructChangeSessionResponse(changeSessionRequest.getVersion(), false, DSCResultCode.NOT_CHANGED);
            } else {
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
                changeSessionResponse = constructChangeSessionResponse(changeSessionRequest.getVersion(), true, DSCResultCode.OK);
            }
        }

        return changeSessionResponse;
    }

    private DSCResultCode terminateSession(String sessionId, String replica, String replicaSet) {
        // TODO when terminating a session, it should be added to a graveyard of some sort to let "getUpdates" know.
        if (sessionId == null) {
            LOG.warning("Terminating a session with missing session id, ignoring request...");
            return DSCResultCode.NOT_CHANGED;
        }
        // Now validate whether the replica set is valid
        Replica cachedReplica = getReplicaFromCache(replica);
        if (cachedReplica == null) {
            LOG.warning("Cached replica is null.");
            return DSCResultCode.REPLICA_SET_NOT_FOUND;
        }
        if (!cachedReplica.getReplicaSet().equalsIgnoreCase(replicaSet)) {
            LOG.warning("replica sets don't match.");
            return DSCResultCode.REPLICA_SET_NOT_FOUND;
        }

        Session cachedSession = getSessionFromCache(sessionId);

        if (cachedSession == null) {
            LOG.fine("Session with ID '" + sessionId + "' has no associated session.");
            return DSCResultCode.NOT_CHANGED;
        }

        LOG.finest("Removing session: " + sessionId);
        sessionsRemoteCache.remove(sessionId);

        return DSCResultCode.OK;
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

    private ChangeSessionResponse constructChangeSessionResponse(int version, boolean updated, DSCResultCode resultCode) {
        ChangeSessionReturn changeSessionReturn;
        boolean clearOnReadDataPresent = false;
        if (updated) {
            changeSessionReturn = new ChangeSessionReturn(
                    resultCode.getResultCode(),
                    version + 1,
                    1,
                    clearOnReadDataPresent
            );
        } else {
            changeSessionReturn = new ChangeSessionReturn(
                    resultCode.getResultCode(),
                    version,
                    1,
                    clearOnReadDataPresent
            );
        }
        return new ChangeSessionResponse(changeSessionReturn);
    }

    private GetSessionResponse constructGetSessionResponse(DSCResultCode resultCode, int version, int stackDepth, ArrayList<GetSessionDataReturn> data) {
        GetSessionReturn getSessionReturn = new GetSessionReturn(
                resultCode.getResultCode(),
                version,
                stackDepth,
                data
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

    private CreateSessionResponse cacheSession(CreateSessionRequest createSessionRequest) {
        // Extract Session ID and verify whether the request contains data
        String sessionId = createSessionRequest.getSessionId();
        if (sessionId == null || createSessionRequest.getData() == null) {
            LOG.info("No session ID or data.");
            return CreateSessionResponse.constructCreateSessionResponse(
                    DSCResultCode.NOT_CREATED.getResultCode()
            );
        }
        LOG.finest("Session ID = " + sessionId);
        // Attempt to get the session from cache
        Session cachedSession = getSessionFromCache(sessionId);
        if (cachedSession != null) {
            LOG.info("Session ID already exists.");
            return CreateSessionResponse.constructCreateSessionResponse(
                    DSCResultCode.NOT_CREATED.getResultCode()
            );
        }
        // Now validate whether the replica set is valid
        Replica cachedReplica = getReplicaFromCache(createSessionRequest.getReplica());
        if (cachedReplica == null) {
            LOG.info("cached replica is null.");
            return CreateSessionResponse.constructCreateSessionResponse(
                    DSCResultCode.REPLICA_SET_NOT_FOUND.getResultCode()
            );
        }
        if (!cachedReplica.getReplicaSet().equalsIgnoreCase(createSessionRequest.getReplicaSet())) {
            LOG.info("replica sets don't match.");
            return CreateSessionResponse.constructCreateSessionResponse(
                    DSCResultCode.REPLICA_SET_NOT_FOUND.getResultCode()
            );
        }
        LOG.info("cached replica set = " + cachedReplica.getReplicaSet());
        LOG.info("requested replica set = " + createSessionRequest.getReplicaSet());
        // Validations succeeded, prepare to cache the session
        // Get the session inactivity timeout first
        long lifetime = getSessionLifetime(createSessionRequest.getData());
        LOG.finest("use session lifetime of " + lifetime);

        // Construct the Session Object to be stored in Infinispan
        cachedSession = new Session(
                createSessionRequest.getSessionId(),
                createSessionRequest.getReplicaSet(),
                createSessionRequest.getSessionLimit(),
                new ArrayList<>(createSessionRequest.getData())
        );

        if (lifetime == 0L) {
            sessionsRemoteCache
//                    .withFlags(Flag.FORCE_RETURN_VALUE) //this will make sure a value is returned, at the cost of marshalling
                    .putIfAbsent(sessionId, cachedSession);

        } else {
            sessionsRemoteCache
//                    .withFlags(Flag.FORCE_RETURN_VALUE) //this will make sure a value is returned, at the cost of marshalling
                    .putIfAbsent(sessionId, cachedSession, lifetime, TimeUnit.SECONDS);
        }
        return CreateSessionResponse.constructCreateSessionResponse(
                DSCResultCode.OK.getResultCode()
        );
    }

    private long getSessionLifetime(List<SessionDataRequest> sessionDataRequestList) {
        AtomicReference<String> sessionInactivityTimeout = new AtomicReference<>();
        sessionDataRequestList.forEach(sessionDataRequest -> {
            if (sessionDataRequest.getDataClass().equalsIgnoreCase(DsessConstants.INACTIVITY_TIMEOUT)) {
                sessionInactivityTimeout.set(sessionDataRequest.getValue());
            }
        });
        // Parse the session inactivity to a long
        long lifetime = 0L;
        try {
            lifetime = Long.parseLong(sessionInactivityTimeout.get().replaceFirst("0x", ""), 16);
        } catch (Exception ignored) {

        }
        return lifetime;
    }

    private Session getSessionFromCache(String sessionId) {
        MetadataValue<Session> sessionMetadataValue = getSessionFromCacheWithMetadata(sessionId);
        return sessionMetadataValue == null ? null : sessionMetadataValue.getValue();
    }

    private MetadataValue<Session> getSessionFromCacheWithMetadata(String sessionId) {
        return sessionsRemoteCache.getWithMetadata(sessionId);
    }

    private Replica getReplicaFromCache(String replica) {
        MetadataValue<Replica> replicaMetadataValue = getReplicaFromCacheWithMetadata(replica);
        return replicaMetadataValue == null ? null : replicaMetadataValue.getValue();
    }

    private MetadataValue<Replica> getReplicaFromCacheWithMetadata(String replica) {
        return replicasRemoteCache.getWithMetadata(replica);
    }

}
