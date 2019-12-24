package be.cronos.model;

import be.cronos.DsessConstants;
import be.cronos.model.ispn.SessionData;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data", namespace = DsessConstants.SMS_NS)
public class SessionDataRequest extends SessionData {

    public SessionDataRequest() {
        super();
    }

    public SessionDataRequest(String dataClass, String value, String instance, int changePolicy) {
        super(dataClass, value, instance, changePolicy);
    }

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
