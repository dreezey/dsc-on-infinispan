package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "joinReplicaSetReturn", namespace = DsessConstants.SMS_NS)
public class JoinReplicaSetReturn {
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int result = DsessConstants.STATIC_RESULT_INT;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final String currentKey = DsessConstants.NEW_KEY;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final String oldKey = DsessConstants.NEW_KEY;
    @XmlElement(namespace = DsessConstants.SMS_NS, name = "currentKeyID")
    private int currentKeyId;
    @XmlElement(namespace = DsessConstants.SMS_NS, name = "oldKeyID")
    private int oldKeyId;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int currentKeyAge;

    public JoinReplicaSetReturn(int currentKeyId, int oldKeyId, int currentKeyAge) {
        this.currentKeyId = currentKeyId;
        this.oldKeyId = oldKeyId;
        this.currentKeyAge = currentKeyAge;
    }

    public JoinReplicaSetReturn() {
        this(0, 0, -1);
    }

    public int getResult() {
        return result;
    }

    public String getCurrentKey() {
        return currentKey;
    }

    public String getOldKey() {
        return oldKey;
    }

    public int getCurrentKeyId() {
        return currentKeyId;
    }

    public void setCurrentKeyId(int currentKeyId) {
        this.currentKeyId = currentKeyId;
    }

    public int getOldKeyId() {
        return oldKeyId;
    }

    public void setOldKeyId(int oldKeyId) {
        this.oldKeyId = oldKeyId;
    }

    public int getCurrentKeyAge() {
        return currentKeyAge;
    }

    public void setCurrentKeyAge(int currentKeyAge) {
        this.currentKeyAge = currentKeyAge;
    }
}
