package be.cronos.model;

import be.cronos.DsessConstants;
import be.cronos.model.ispn.SessionData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement(name = "data", namespace = DsessConstants.SMS_NS)
public class SessionDataRequest extends SessionData {

//    @XmlElement(namespace = DsessConstants.SMS_NS, nillable = true)
//    private String value;
//    @XmlElement(namespace = DsessConstants.SMS_NS)
//    private String dataClass;
//    @XmlElement(namespace = DsessConstants.SMS_NS)
//    private String instance;
//    @XmlElement(namespace = DsessConstants.SMS_NS)
//    private int changePolicy;


    public SessionDataRequest() {
        super();
    }

    public SessionDataRequest(String dataClass, String value, String instance, int changePolicy) {
        super(dataClass, value, instance, changePolicy);
    }

    //    public CreateSessionDataRequest() {
//    }
//
//    public CreateSessionDataRequest(String value, String dataClass, String instance, int changePolicy) {
//        this.value = value;
//        this.dataClass = dataClass;
//        this.instance = instance;
//        this.changePolicy = changePolicy;
//    }

    @Override
    @XmlElement(namespace = DsessConstants.SMS_NS, nillable = true)
    public String getValue() {
        return super.getValue();
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    @Override
    @XmlElement(namespace = DsessConstants.SMS_NS)
    public String getDataClass() {
        return super.getDataClass();
    }

    @Override
    public void setDataClass(String dataClass) {
        super.setDataClass(dataClass);
    }

    @Override
    @XmlElement(namespace = DsessConstants.SMS_NS)
    public String getInstance() {
        return super.getInstance();
    }

    @Override
    public void setInstance(String instance) {
        super.setInstance(instance);
    }

    @Override
    @XmlElement(namespace = DsessConstants.SMS_NS)
    public int getChangePolicy() {
        return super.getChangePolicy();
    }

    @Override
    public void setChangePolicy(int changePolicy) {
        super.setChangePolicy(changePolicy);
    }
}
