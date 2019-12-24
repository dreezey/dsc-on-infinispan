package be.cronos.model.ispn;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import javax.xml.bind.annotation.XmlTransient;
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
        this.dataClass = Objects.requireNonNull(dataClass);
        this.value = value;
        this.instance = Objects.requireNonNull(instance);
        this.changePolicy = changePolicy;
    }

    @ProtoField(number = 1)
    @XmlTransient // Annotation is here to prevent it from being marshalled in a response.
    public String getDataClass() {
        return dataClass;
    }

    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
    }

    @ProtoField(number = 2)
    @XmlTransient // Annotation is here to prevent it from being marshalled in a response.
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @ProtoField(number = 3)
    @XmlTransient // Annotation is here to prevent it from being marshalled in a response.
    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    @ProtoField(number = 4, defaultValue = "1")
    @XmlTransient // Annotation is here to prevent it from being marshalled in a response.
    public int getChangePolicy() {
        return changePolicy;
    }

    public void setChangePolicy(int changePolicy) {
        this.changePolicy = changePolicy;
    }

    @Override
    public String toString() {
        return "SessionData{" +
                "dataClass='" + dataClass + '\'' +
                ", value='" + value + '\'' +
                ", instance='" + instance + '\'' +
                ", changePolicy=" + changePolicy +
                '}';
    }
}
