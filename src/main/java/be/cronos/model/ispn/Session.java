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

import java.util.ArrayList;
import java.util.Objects;

public class Session {
    private String sessionId;
    private String replicaSet;
    private int sessionLimit;
    private ArrayList<SessionData> sessionData;
    private int version;

    public Session() {
    }

    @ProtoFactory
    public Session(String sessionId, String replicaSet, int sessionLimit, ArrayList<SessionData> sessionData, int version) {
        this.sessionId = Objects.requireNonNull(sessionId);
        this.replicaSet = Objects.requireNonNull(replicaSet);
        this.sessionLimit = sessionLimit;
        this.sessionData = Objects.requireNonNull(sessionData);
        this.version = version;
    }

    public static Session shallowCopy(Session sourceSession) {
        return new Session(
                sourceSession.sessionId,
                sourceSession.replicaSet,
                sourceSession.sessionLimit,
                sourceSession.getSessionData(),
                sourceSession.version
        );
    }

    @ProtoField(number = 1, required = true)
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @ProtoField(number = 2, required = true)
    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }

    @ProtoField(number = 3, collectionImplementation = ArrayList.class)
    public ArrayList<SessionData> getSessionData() {
        return sessionData;
    }

    public void setSessionData(ArrayList<SessionData> sessionData) {
        this.sessionData = sessionData;
    }

    @ProtoField(number = 4, defaultValue = "0")
    public int getSessionLimit() {
        return sessionLimit;
    }

    public void setSessionLimit(int sessionLimit) {
        this.sessionLimit = sessionLimit;
    }

    @ProtoField(number = 5, defaultValue = "0")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", replicaSet='" + replicaSet + '\'' +
                ", sessionLimit=" + sessionLimit +
                ", sessionData=" + sessionData +
                ", version=" + version +
                '}';
    }
}
