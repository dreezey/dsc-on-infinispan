package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "changeSession", namespace = DsessConstants.SMS_NS)
public class ChangeSessionRequest {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replica;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replicaSet;
    @XmlElement(name = "sessionID", namespace = DsessConstants.SMS_NS)
    private String sessionId;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int version;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private boolean push;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int sessionLimit;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private List<SessionDataRequest> data;

    public ChangeSessionRequest() {
    }

    public ChangeSessionRequest(String replica, String replicaSet, String sessionId, int version, boolean push, int sessionLimit, List<SessionDataRequest> data) {
        this.replica = replica;
        this.replicaSet = replicaSet;
        this.sessionId = sessionId;
        this.version = version;
        this.push = push;
        this.sessionLimit = sessionLimit;
        this.data = data;
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public int getSessionLimit() {
        return sessionLimit;
    }

    public void setSessionLimit(int sessionLimit) {
        this.sessionLimit = sessionLimit;
    }

    public List<SessionDataRequest> getData() {
        return data;
    }

    public void setData(List<SessionDataRequest> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ChangeSessionRequest{" +
                "replica='" + replica + '\'' +
                ", replicaSet='" + replicaSet + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", version=" + version +
                ", push=" + push +
                ", sessionLimit=" + sessionLimit +
                ", data=" + data +
                '}';
    }
}
