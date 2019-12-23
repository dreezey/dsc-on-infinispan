package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getSessionReturn", namespace = DsessConstants.SMS_NS)
public class GetSessionReturn {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int result = DsessConstants.STATIC_RESULT_INT;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int version;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int stackDepth = 1;
    @XmlElement(namespace = DsessConstants.SMS_NS, nillable = true, required = false)
    private ArrayList<GetSessionDataReturn> data;

    public GetSessionReturn() {
    }

    public GetSessionReturn(int version, ArrayList<GetSessionDataReturn> data) {
        this.version = version;
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public int getStackDepth() {
        return stackDepth;
    }

    public ArrayList<GetSessionDataReturn> getData() {
        return data;
    }

    public void setData(ArrayList<GetSessionDataReturn> data) {
        this.data = data;
    }
}
