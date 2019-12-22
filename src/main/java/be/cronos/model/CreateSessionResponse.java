package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "createSessionResponse", namespace = DsessConstants.SMS_NS)
public class CreateSessionResponse {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private CreateSessionReturn createSessionReturn;

    public CreateSessionResponse() {
        this.createSessionReturn = new CreateSessionReturn();
    }

    public CreateSessionResponse(CreateSessionReturn createSessionReturn) {
        this.createSessionReturn = createSessionReturn;
    }

    public CreateSessionReturn getCreateSessionReturn() {
        return createSessionReturn;
    }

    public void setCreateSessionReturn(CreateSessionReturn createSessionReturn) {
        this.createSessionReturn = createSessionReturn;
    }
}
