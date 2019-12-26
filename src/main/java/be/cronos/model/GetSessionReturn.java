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
    private int result;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int version;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int stackDepth;
    @XmlElement(namespace = DsessConstants.SMS_NS, nillable = true, required = false)
    private ArrayList<GetSessionDataReturn> data;

    public GetSessionReturn() {
    }

    public GetSessionReturn(int result, int version, int stackDepth, ArrayList<GetSessionDataReturn> data) {
        this.result = result;
        this.version = version;
        this.stackDepth = stackDepth;
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getStackDepth() {
        return stackDepth;
    }

    public void setStackDepth(int stackDepth) {
        this.stackDepth = stackDepth;
    }

    public ArrayList<GetSessionDataReturn> getData() {
        return data;
    }

    public void setData(ArrayList<GetSessionDataReturn> data) {
        this.data = data;
    }
}
