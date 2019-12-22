package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "replicaShutdownResponse", namespace = DsessConstants.SMS_NS)
public class ReplicaShutdownResponse {
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int replicaShutdownReturn = DsessConstants.STATIC_RESULT_INT;

    public ReplicaShutdownResponse() {
    }

//    public ReplicaShutdownResponse(int replicaShutdownReturn) {
//        this.replicaShutdownReturn = replicaShutdownReturn;
//    }

    public int getReplicaShutdownReturn() {
        return replicaShutdownReturn;
    }

//    public void setReplicaShutdownReturn(int replicaShutdownReturn) {
//        this.replicaShutdownReturn = replicaShutdownReturn;
//    }
}
