package be.cronos.resources;

import be.cronos.DsessConstants;
import be.cronos.model.*;
import be.cronos.services.GetUpdatesService;
import be.cronos.services.PingService;
import be.cronos.services.DSessService;
import org.jboss.logmanager.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.*;

@Path("/DSess")
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
    public CompletionStage<String> health() {
        return CompletableFuture.supplyAsync(() -> {
            return "healthy";
        });
    }

    @Path("ping")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public CompletionStage<Response> ping(PingRequest pingRequest) {
        LOG.info("ping");
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
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        LOG.finest("Response by = " + getUpdatesRequest.getResponseBy() );
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

        return CompletableFuture.supplyAsync(() -> {
            // TODO implement session termination
            return DSessService.terminateSession(terminateSessionRequest);
        });
    }

    @Path("changeSession")
    @POST
    @Consumes(MediaType.TEXT_XML)
    @Produces({DsessConstants.CUSTOM_XML_MIMETYPE, MediaType.TEXT_XML})
    public CompletionStage<Response> changeSession(ChangeSessionRequest changeSessionRequest) {
        LOG.entering(DSessResource.class.getName(), "changeSession");

        return CompletableFuture.supplyAsync(() -> {
            return DSessService.changeSession(changeSessionRequest);
        });
    }
}