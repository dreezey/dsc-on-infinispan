package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getSessionResponse", namespace = DsessConstants.SMS_NS)
public class GetSessionResponse {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private GetSessionReturn getSessionReturn;

    public GetSessionResponse() {
        this.getSessionReturn = new GetSessionReturn();
    }

    public GetSessionResponse(GetSessionReturn getSessionReturn) {
        this.getSessionReturn = getSessionReturn;
    }

    public GetSessionReturn getCreateSessionReturn() {
        return getSessionReturn;
    }

    public void setCreateSessionReturn(GetSessionReturn getSessionReturn) {
        this.getSessionReturn = getSessionReturn;
    }
}
