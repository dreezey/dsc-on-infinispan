package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "changeSessionResponse", namespace = DsessConstants.SMS_NS)
public class ChangeSessionResponse {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private ChangeSessionReturn changeSessionReturn;

    public ChangeSessionResponse() {
        this.changeSessionReturn = new ChangeSessionReturn();
    }

    public ChangeSessionResponse(ChangeSessionReturn changeSessionReturn) {
        this.changeSessionReturn = changeSessionReturn;
    }

    public ChangeSessionReturn getChangeSessionReturn() {
        return changeSessionReturn;
    }

    public void setChangeSessionReturn(ChangeSessionReturn changeSessionReturn) {
        this.changeSessionReturn = changeSessionReturn;
    }
}
