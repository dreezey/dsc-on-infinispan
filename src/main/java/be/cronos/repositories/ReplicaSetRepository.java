package be.cronos.repositories;

import be.cronos.model.ispn.ReplicaSet;
import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.jboss.resteasy.annotations.cache.Cache;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class ReplicaSetRepository {

    @Inject
    @Remote("dsess")
    private RemoteCache<String, ReplicaSet> replicaSets;

    private RemoteCacheManager remoteCacheManager;

    @Inject
    public ReplicaSetRepository(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }
}
