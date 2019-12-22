package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getRealmNameReturn", namespace = DsessConstants.SMS_NS)
public class GetRealmNameReturn {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final int result = DsessConstants.STATIC_RESULT_INT;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private final String realm = DsessConstants.REALM_NAME;

    public GetRealmNameReturn() {
    }

    public int getResult() {
        return result;
    }

    public String getRealm() {
        return realm;
    }
}
