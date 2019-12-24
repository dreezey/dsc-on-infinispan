package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getUpdatesResponse", namespace = DsessConstants.SMS_NS)
public class GetUpdatesResponse {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private GetUpdatesReturn getUpdatesReturn;

    public GetUpdatesResponse(GetUpdatesReturn getUpdatesReturn) {
        this.getUpdatesReturn = getUpdatesReturn;
    }

    public GetUpdatesResponse() {
        this(new GetUpdatesReturn(DsessConstants.STATIC_RESULT_INT, DsessConstants.NEW_KEY, 0));
    }

    public GetUpdatesReturn getGetUpdatesReturn() {
        return getUpdatesReturn;
    }

    public void setGetUpdatesReturn(GetUpdatesReturn getUpdatesReturn) {
        this.getUpdatesReturn = getUpdatesReturn;
    }
}
