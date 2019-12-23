package be.cronos.model;

import be.cronos.DsessConstants;
import be.cronos.model.ispn.SessionData;

import javax.xml.bind.annotation.*;

//@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
//@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(
        name = "data",
        propOrder = {
                "value",
                "dataClass",
                "instance"
        },
        namespace = DsessConstants.SMS_NS
)
//@XmlRootElement(name = "data", namespace = DsessConstants.SMS_NS)
public class GetSessionDataReturn extends SessionData {

    public GetSessionDataReturn() {
        super();
    }

    public GetSessionDataReturn(String dataClass, String value, String instance, int changePolicy) {
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

}
