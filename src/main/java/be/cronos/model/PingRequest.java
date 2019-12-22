package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ping", namespace = DsessConstants.SMS_NS)
public class PingRequest {
    @XmlElement(namespace = DsessConstants.SMS_NS)
    public int something;

    public PingRequest() {
    }

    public PingRequest(int something) {
        this.something = something;
    }


    public int getSomething() {
        return something;
    }

    public void setSomething(int something) {
        this.something = something;
    }
}
