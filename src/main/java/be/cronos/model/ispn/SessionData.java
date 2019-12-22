package be.cronos.model.ispn;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.Objects;

public class SessionData {
    private String dataClass;
    private String value;
    private String instance;
    private int changePolicy;

    public SessionData() {
    }

    @ProtoFactory
    public SessionData(String dataClass, String value, String instance, int changePolicy) {
//        this.sessionId = Objects.requireNonNull(sessionId);
        this.dataClass = Objects.requireNonNull(dataClass);
        this.value = Objects.requireNonNull(value);
        this.instance = Objects.requireNonNull(instance);
        this.changePolicy = changePolicy;
    }

//    @ProtoField(number = 1)
//    public String getSessionId() {
//        return sessionId;
//    }
//
//    public void setSessionId(String sessionId) {
//        this.sessionId = sessionId;
//    }

    @ProtoField(number = 1)
    public String getDataClass() {
        return dataClass;
    }

    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
    }

    @ProtoField(number = 2)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @ProtoField(number = 3)
    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    @ProtoField(number = 4, defaultValue = "1")
    public int getChangePolicy() {
        return changePolicy;
    }

    public void setChangePolicy(int changePolicy) {
        this.changePolicy = changePolicy;
    }
}
