package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "createSessionReturn", namespace = DsessConstants.SMS_NS)
public class CreateSessionReturn {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int result = DsessConstants.STATIC_RESULT_INT;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int version = 0;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int stackDepth = 1;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final boolean clearOnReadDataPresent = false;

    public CreateSessionReturn() {
    }

    public int getResult() {
        return result;
    }

    public int getVersion() {
        return version;
    }

    public int getStackDepth() {
        return stackDepth;
    }

    public boolean isClearOnReadDataPresent() {
        return clearOnReadDataPresent;
    }
}
