package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getRealmName", namespace = DsessConstants.SMS_NS)
public class GetRealmNameRequest {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replica;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replicaSet;

    public GetRealmNameRequest() {
    }

    public GetRealmNameRequest(String replica, String replicaSet) {
        this.replica = replica;
        this.replicaSet = replicaSet;
    }

    public String getReplica() {
        return replica;
    }

    public void setReplica(String replica) {
        this.replica = replica;
    }

    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }

    @Override
    public String toString() {
        return "GetRealmNameRequest{" +
                "replica='" + replica + '\'' +
                ", replicaSet='" + replicaSet + '\'' +
                '}';
    }
}
