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
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getSession", namespace = DsessConstants.SMS_NS)
public class GetSessionRequest {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replica;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replicaSet;
    @XmlElement(name = "sessionID", namespace = DsessConstants.SMS_NS)
    private String sessionId;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String ssoType;
    @XmlElement(namespace = DsessConstants.SMS_NS, nillable = true)
    private String ssoSource;

    public GetSessionRequest() {
    }

    public GetSessionRequest(String replica, String replicaSet, String sessionId, String ssoType, String ssoSource) {
        this.replica = replica;
        this.replicaSet = replicaSet;
        this.sessionId = sessionId;
        this.ssoType = ssoType;
        this.ssoSource = ssoSource;
    }

    public String getReplica() {
        return replica;
    }

    public void setReplica(String replica) {
        this.replica = replica;
    }

    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSsoType() {
        return ssoType;
    }

    public void setSsoType(String ssoType) {
        this.ssoType = ssoType;
    }

    public String getSsoSource() {
        return ssoSource;
    }

    public void setSsoSource(String ssoSource) {
        this.ssoSource = ssoSource;
    }

    @Override
    public String toString() {
        return "GetSessionRequest{" +
                "replica='" + replica + '\'' +
                ", replicaSet='" + replicaSet + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", ssoType='" + ssoType + '\'' +
                ", ssoSource='" + ssoSource + '\'' +
                '}';
    }
}
