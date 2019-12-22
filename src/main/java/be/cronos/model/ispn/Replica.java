package be.cronos.model.ispn;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.Objects;

public class Replica {
    private String replica;
    private String instance;
    private int capabilities;
    private String replicaSet;

    @ProtoFactory
    public Replica(String replica, String instance, int capabilities, String replicaSet) {
        this.replica = Objects.requireNonNull(replica);
        this.instance = Objects.requireNonNull(instance);
        this.capabilities = capabilities;
        this.replicaSet = Objects.requireNonNull(replicaSet);
    }

    @ProtoField(number = 1)
    public String getReplica() {
        return replica;
    }

    public void setReplica(String replica) {
        this.replica = replica;
    }

    @ProtoField(number = 2)
    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    @ProtoField(number = 3, defaultValue = "15")
    public int getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(int capabilities) {
        this.capabilities = capabilities;
    }

    @ProtoField(number = 4)
    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }

    @Override
    public String toString() {
        return "Replica{" +
                "replica='" + replica + '\'' +
                ", instance='" + instance + '\'' +
                ", capabilities=" + capabilities +
                ", replicaSet='" + replicaSet + '\'' +
                '}';
    }
}
