package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getRealmNameResponse", namespace = DsessConstants.SMS_NS)
public class GetRealmNameResponse {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private GetRealmNameReturn getRealmNameReturn;

    public GetRealmNameResponse() {
        this.getRealmNameReturn = new GetRealmNameReturn();
    }

    public GetRealmNameResponse(GetRealmNameReturn getRealmNameReturn) {
        this.getRealmNameReturn = getRealmNameReturn;
    }

    public GetRealmNameReturn getGetRealmNameReturn() {
        return getRealmNameReturn;
    }

    public void setGetRealmNameReturn(GetRealmNameReturn getRealmNameReturn) {
        this.getRealmNameReturn = getRealmNameReturn;
    }
}
