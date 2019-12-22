package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "replicaShutdown", namespace = DsessConstants.SMS_NS)
public class ReplicaShutdownRequest {
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replica;

    public ReplicaShutdownRequest() {
    }

    public ReplicaShutdownRequest(String replica) {
        this.replica = replica;
    }

    public String getReplica() {
        return replica;
    }

    public void setReplica(String replica) {
        this.replica = replica;
    }
}
