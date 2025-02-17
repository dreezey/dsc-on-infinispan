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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "terminateSession", namespace = DsessConstants.SMS_NS)
public class TerminateSessionRequest extends SessionRequestBase {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int level;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int version;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int reason;

    public TerminateSessionRequest() {
    }

    public TerminateSessionRequest(String replica, String replicaSet, String sessionId, int level, int version, int reason) {
        this.replica = replica;
        this.replicaSet = replicaSet;
        this.sessionId = sessionId;
        this.level = level;
        this.version = version;
        this.reason = reason;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "TerminateSessionRequest{" +
                "replica='" + replica + '\'' +
                ", replicaSet='" + replicaSet + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", level=" + level +
                ", version=" + version +
                ", reason=" + reason +
                '}';
    }
}
