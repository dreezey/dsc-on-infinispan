package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "createSession", namespace = DsessConstants.SMS_NS)
public class CreateSessionRequest {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replica;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replicaSet;
    @XmlElement(name = "sessionID",namespace = DsessConstants.SMS_NS)
    private String sessionId;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int sessionLimit;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private List<SessionDataRequest> data;

    public CreateSessionRequest() {
    }

    public CreateSessionRequest(String replica, String replicaSet, String sessionId, int sessionLimit, List<SessionDataRequest> data) {
        this.replica = replica;
        this.replicaSet = replicaSet;
        this.sessionId = sessionId;
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
        return "CreateSessionRequest{" +
                "replica='" + replica + '\'' +
                ", replicaSet='" + replicaSet + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", sessionLimit=" + sessionLimit +
                ", data=" + data +
                '}';
    }
}
