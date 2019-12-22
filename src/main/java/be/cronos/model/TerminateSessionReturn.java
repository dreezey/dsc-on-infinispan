package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "terminateSessionReturn", namespace = DsessConstants.SMS_NS)
public class TerminateSessionReturn {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int result = DsessConstants.STATIC_RESULT_INT;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int version;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int stackDepth = 0;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final boolean clearOnReadDataPresent = false;

    public TerminateSessionReturn() {
    }

    public TerminateSessionReturn(int version) {
        this.version = version;
    }

    public int getResult() {
        return result;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getStackDepth() {
        return stackDepth;
    }

    public boolean isClearOnReadDataPresent() {
        return clearOnReadDataPresent;
    }
}
