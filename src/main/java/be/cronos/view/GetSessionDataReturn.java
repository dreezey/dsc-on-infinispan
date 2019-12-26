package be.cronos.view;

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

import be.cronos.DsessConstants;
import be.cronos.model.ispn.SessionData;

import javax.xml.bind.annotation.*;

@XmlType(
        name = "data",
        propOrder = {
                "value",
                "dataClass",
                "instance"
        },
        namespace = DsessConstants.SMS_NS
)
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
