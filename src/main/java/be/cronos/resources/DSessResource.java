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

package be.cronos.resources;

import be.cronos.DsessConstants;
import be.cronos.model.*;
import be.cronos.services.GetUpdatesService;
import be.cronos.services.PingService;
import be.cronos.services.DSessService;
import org.jboss.logmanager.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.*;

@Path("/DSess")
@RolesAllowed("replica")
public class DSessResource {
    private static final Logger LOG = Logger.getLogger(DSessResource.class.getName());

    @Inject
    PingService pingService;
    @Inject
    GetUpdatesService getUpdatesService;
    @Inject
    DSessService DSessService;

    @Path("health")
    @GET
    @Produces(MediaType.TEXT_XML)
    @PermitAll // This is for future k8s health probes
    public CompletionStage<String> health() {
        return CompletableFuture.supplyAsync(() -> {
            return "healthy";
        });
    }

    @Path("ping")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    @PermitAll // Everyone should be able to ping...
    public CompletionStage<Response> ping(PingRequest pingRequest) {
        LOG.entering(DSessResource.class.getName(), "ping");
        LOG.finest(pingRequest.toString());
        return CompletableFuture.supplyAsync(() -> {
            return pingService.pong(pingRequest);
        });
    }

    @Path("getUpdates")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public void getUpdates(@Suspended AsyncResponse ar, GetUpdatesRequest getUpdatesRequest) {
        LOG.entering(DSessResource.class.getName(), "getUpdates");
        LOG.finest("getUpdates()");
        LOG.finest(getUpdatesRequest.toString());
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        int responseBy = getUpdatesRequest.getResponseBy();

        // Schedule based on requested responseBy
        executor.schedule(
                () -> { ar.resume(getUpdatesService.getUpdates(getUpdatesRequest)); },
                responseBy,
                TimeUnit.SECONDS
        );
    }

    @Path("joinReplicaSet")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public CompletionStage<Response> joinReplicaSet(JoinReplicaSetRequest joinReplicaSetRequest) {
        LOG.entering(DSessResource.class.getName(), "joinReplicaSet");
        LOG.finest("joinReplicaSet()");
        LOG.finest(joinReplicaSetRequest.toString());

        return CompletableFuture.supplyAsync(() -> {
            return DSessService.joinReplicaSet(joinReplicaSetRequest);
        });
    }

    @Path("replicaShutdown")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public CompletionStage<Response> replicaShutdown(ReplicaShutdownRequest replicaShutdownRequest) {
        LOG.entering(DSessResource.class.getName(), "replicaShutdown");
        LOG.finest("replicaShutdown()");
        LOG.finest(replicaShutdownRequest.toString());

        return CompletableFuture.supplyAsync(() -> {
            return DSessService.shutdownReplica(replicaShutdownRequest);
        });
    }

    @Path("getRealmName")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public CompletionStage<Response> getRealmName(GetRealmNameRequest getRealmNameRequest) {
        LOG.entering(DSessResource.class.getName(), "getRealmName");
        LOG.finest("getRealmName()");
        LOG.finest(getRealmNameRequest.toString());

        return CompletableFuture.supplyAsync(() -> {
            return DSessService.getRealmName(getRealmNameRequest);
        });
    }

    @Path("createSession")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public CompletionStage<Response> createSession(CreateSessionRequest createSessionRequest) {
        LOG.entering(DSessResource.class.getName(), "createSession");
        LOG.finest("createSession()");
        LOG.finest(createSessionRequest.toString());

        return CompletableFuture.supplyAsync(() -> {
            return DSessService.createSession(createSessionRequest);
        });
    }

    @Path("getSession")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public CompletionStage<Response> getSession(GetSessionRequest getSessionRequest) {
        LOG.entering(DSessResource.class.getName(), "getSession");
        LOG.finest("getSession()");
        LOG.finest(getSessionRequest.toString());

        return CompletableFuture.supplyAsync(() -> {
            return DSessService.getSession(getSessionRequest);
        });
    }

    @Path("idleTimeout")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public CompletionStage<Response> idleTimeout(IdleTimeoutRequest idleTimeoutRequest) {
        LOG.entering(DSessResource.class.getName(), "idleTimeout");
        LOG.finest("idleTimeout()");
        LOG.finest(idleTimeoutRequest.toString());

        return CompletableFuture.supplyAsync(() -> {
            return DSessService.idleTimeout(idleTimeoutRequest);
        });
    }

    @Path("terminateSession")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public CompletionStage<Response> terminateSession(TerminateSessionRequest terminateSessionRequest) {
        LOG.entering(DSessResource.class.getName(), "terminateSession");
        LOG.finest("terminateSession()");
        LOG.finest(terminateSessionRequest.toString());

        return CompletableFuture.supplyAsync(() -> {
            return DSessService.terminateSession(terminateSessionRequest);
        });
    }

    @Path("changeSession")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public CompletionStage<Response> changeSession(ChangeSessionRequest changeSessionRequest) {
        LOG.entering(DSessResource.class.getName(), "changeSession");
        LOG.finest("changeSession()");
        LOG.finest(changeSessionRequest.toString());

        return CompletableFuture.supplyAsync(() -> {
            return DSessService.changeSession(changeSessionRequest);
        });
    }
}