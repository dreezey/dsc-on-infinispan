package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getSession", namespace = DsessConstants.SMS_NS)
public class GetSessionRequest {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replica;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replicaSet;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String sessionId;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String ssoType;
    @XmlElement(namespace = DsessConstants.SMS_NS, nillable = true)
    private String ssoSource;

    public GetSessionRequest() {
    }

    public GetSessionRequest(String replica, String replicaSet, String sessionId, String ssoType, String ssoSource) {
        this.replica = replica;
        this.replicaSet = replicaSet;
        this.sessionId = sessionId;
        this.ssoType = ssoType;
        this.ssoSource = ssoSource;
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

    public String getSsoType() {
        return ssoType;
    }

    public void setSsoType(String ssoType) {
        this.ssoType = ssoType;
    }

    public String getSsoSource() {
        return ssoSource;
    }

    public void setSsoSource(String ssoSource) {
        this.ssoSource = ssoSource;
    }
}
