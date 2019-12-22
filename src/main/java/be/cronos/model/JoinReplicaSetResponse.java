package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "joinReplicaSetResponse", namespace = DsessConstants.SMS_NS)
public class JoinReplicaSetResponse {
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private JoinReplicaSetReturn joinReplicaSetReturn;

    public JoinReplicaSetResponse(JoinReplicaSetReturn joinReplicaSetReturn) {
        this.joinReplicaSetReturn = joinReplicaSetReturn;
    }

    public JoinReplicaSetResponse() {
        this.joinReplicaSetReturn = new JoinReplicaSetReturn();
    }

    public JoinReplicaSetReturn getJoinReplicaSetReturn() {
        return joinReplicaSetReturn;
    }

    public void setJoinReplicaSetReturn(JoinReplicaSetReturn joinReplicaSetReturn) {
        this.joinReplicaSetReturn = joinReplicaSetReturn;
    }
}
