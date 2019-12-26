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

@XmlAccessorType(XmlAccessType.FIELD)
public class TerminationsDataReturn {
    /**
     * <ns1:terminations>
     *     <ns1:replicaSet>dsc-test</ns1:replicaSet>
     *     <ns1:sessionID>LXoBcBXkyDhKqfirUpZy12vgNlPm2sW17zKIXnNCxNVKuFZxy2o=</ns1:sessionID>
     * </ns1:terminations>
     */

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replicaSet;
    @XmlElement(name = "sessionID", namespace = DsessConstants.SMS_NS)
    private String sessionId;

    public TerminationsDataReturn() {
    }

    public TerminationsDataReturn(String replicaSet, String sessionId) {
        this.replicaSet = replicaSet;
        this.sessionId = sessionId;
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
}
