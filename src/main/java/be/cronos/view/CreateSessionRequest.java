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

package be.cronos.view;

import be.cronos.DsessConstants;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "createSession", namespace = DsessConstants.SMS_NS)
public class CreateSessionRequest extends SessionRequestBase {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int sessionLimit;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private List<SessionDataRequest> data;

    public CreateSessionRequest() {
    }

    public CreateSessionRequest(String replica, String replicaSet, String sessionId, int sessionLimit, List<SessionDataRequest> data) {
        this.replica = replica;
        this.replicaSet = replicaSet;
        this.sessionId = sessionId;
        this.sessionLimit = sessionLimit;
        this.data = data;
    }

    public int getSessionLimit() {
        return sessionLimit;
    }

    public void setSessionLimit(int sessionLimit) {
        this.sessionLimit = sessionLimit;
    }

    public List<SessionDataRequest> getData() {
        return data;
    }

    public void setData(List<SessionDataRequest> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CreateSessionRequest{" +
                "replica='" + replica + '\'' +
                ", replicaSet='" + replicaSet + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", sessionLimit=" + sessionLimit +
                ", data=" + data +
                '}';
    }
}
