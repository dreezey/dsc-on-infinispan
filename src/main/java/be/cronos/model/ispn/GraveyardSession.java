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

public class GraveyardSession {
    private String sessionId;
    private String replicaSet;

    public GraveyardSession() {
    }

    @ProtoFactory
    public GraveyardSession(String sessionId, String replicaSet) {
        this.sessionId = sessionId;
        this.replicaSet = replicaSet;
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

}
