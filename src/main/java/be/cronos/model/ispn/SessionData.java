/*
 * Copyright 2019 IS4U NV. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
