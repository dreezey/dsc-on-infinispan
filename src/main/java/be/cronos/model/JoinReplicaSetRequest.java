package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "joinReplicaSet", namespace = DsessConstants.SMS_NS)
public class JoinReplicaSetRequest {
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replica;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String instance;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int capabilities;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replicaSet;

    public JoinReplicaSetRequest() {
    }

    public JoinReplicaSetRequest(String replica, String instance, int capabilities, String replicaSet) {
        this.replica = replica;
        this.instance = instance;
        this.capabilities = capabilities;
        this.replicaSet = replicaSet;
    }

    public String getReplica() {
        return replica;
    }

    public void setReplica(String replica) {
        this.replica = replica;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public int getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(int capabilities) {
        this.capabilities = capabilities;
    }

    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }

    @Override
    public String toString() {
        return "JoinReplicaSetRequest{" +
                "replica='" + replica + '\'' +
                ", instance='" + instance + '\'' +
                ", capabilities=" + capabilities +
                ", replicaSet='" + replicaSet + '\'' +
                '}';
    }
}
