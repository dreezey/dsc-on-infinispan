package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "idleTimeoutResponse", namespace = DsessConstants.SMS_NS)
public class IdleTimeoutResponse {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int idleTimeoutReturn = DsessConstants.STATIC_RESULT_INT;

    public IdleTimeoutResponse() {
    }

    public int getIdleTimeoutReturn() {
        return idleTimeoutReturn;
    }
}
