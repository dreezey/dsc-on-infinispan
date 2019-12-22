package be.cronos.services;

import be.cronos.model.*;
import be.cronos.model.ispn.Replica;
import be.cronos.model.ispn.Session;
import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

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

    private List<Replica> searchReplicasByReplicaSet(String replicaSet) {
        QueryFactory queryFactory = Search.getQueryFactory(replicasRemoteCache);
        Query query = queryFactory
                .from(Replica.class)
                .having("replicaSet").eq(replicaSet)
                .build();
        return query.list();
    }

}
