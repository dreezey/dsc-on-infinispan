package be.cronos.model;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {
                "result",
                "newKey",
                "newKeyId"
        }
)
@XmlRootElement(name = "getUpdatesReturn", namespace = DsessConstants.SMS_NS)
public class GetUpdatesReturn {

    @XmlElement(name = "result", namespace = DsessConstants.SMS_NS)
    private int result;
    @XmlElement(name = "newKey", namespace = DsessConstants.SMS_NS)
    private String newKey;
    @XmlElement(name = "newKeyID", namespace = DsessConstants.SMS_NS)
    private int newKeyId;
    /** TODO, there could be 2 more elements here:
     *      <element maxOccurs="unbounded" name="updates" nillable="true" type="impl:SessionUpdate"/>
     *      <element maxOccurs="unbounded" name="terminations" nillable="true" type="impl:SessionKey"/>
     * */

    public GetUpdatesReturn() {
    }

    public GetUpdatesReturn(int result, String newKey, int newKeyId) {
        this.result = result;
        this.newKey = newKey;
        this.newKeyId = newKeyId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getNewKey() {
        return newKey;
    }

    public void setNewKey(String newKey) {
        this.newKey = newKey;
    }

    public int getNewKeyId() {
        return newKeyId;
    }

    public void setNewKeyId(int newKeyId) {
        this.newKeyId = newKeyId;
    }
}
