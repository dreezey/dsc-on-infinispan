package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "terminateSessionResponse", namespace = DsessConstants.SMS_NS)
public class TerminateSessionResponse {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private TerminateSessionReturn terminateSessionReturn;

    public TerminateSessionResponse() {
        this.terminateSessionReturn = new TerminateSessionReturn();
    }

    public TerminateSessionResponse(int version) {
        this.terminateSessionReturn = new TerminateSessionReturn(version);
    }

    public TerminateSessionResponse(TerminateSessionReturn terminateSessionReturn) {
        this.terminateSessionReturn = terminateSessionReturn;
    }

    public TerminateSessionReturn getTerminateSessionReturn() {
        return terminateSessionReturn;
    }

    public void setTerminateSessionReturn(TerminateSessionReturn terminateSessionReturn) {
        this.terminateSessionReturn = terminateSessionReturn;
    }
}
