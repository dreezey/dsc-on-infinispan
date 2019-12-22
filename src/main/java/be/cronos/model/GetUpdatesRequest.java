package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getUpdates", namespace = DsessConstants.SMS_NS)
public class GetUpdatesRequest {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replica;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String instance;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replicaSet;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int responseBy;

    public GetUpdatesRequest() {
    }

    public GetUpdatesRequest(String replica, String instance, String replicaSet, int responseBy) {
        this.replica = replica;
        this.instance = instance;
        this.replicaSet = replicaSet;
        this.responseBy = responseBy;
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

    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }

    public int getResponseBy() {
        return responseBy;
    }

    public void setResponseBy(int responseBy) {
        this.responseBy = responseBy;
    }
}
