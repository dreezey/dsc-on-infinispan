package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pingResponse", namespace = DsessConstants.SMS_NS)
public class PingResponse {
    @XmlElement(name = "pingReturn", namespace = DsessConstants.SMS_NS)
    private int pingReturn;

    public PingResponse(int pingReturn) {
        this.pingReturn = pingReturn;
    }

    public PingResponse() {
        this(DsessConstants.STATIC_RESULT_INT);
    }

    public int getPingReturn() {
        return pingReturn;
    }

    public void setPingReturn(int pingReturn) {
        this.pingReturn = pingReturn;
    }
}
