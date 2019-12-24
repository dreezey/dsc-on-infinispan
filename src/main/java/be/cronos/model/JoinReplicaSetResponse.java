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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "joinReplicaSetResponse", namespace = DsessConstants.SMS_NS)
public class JoinReplicaSetResponse {
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private JoinReplicaSetReturn joinReplicaSetReturn;

    public JoinReplicaSetResponse(JoinReplicaSetReturn joinReplicaSetReturn) {
        this.joinReplicaSetReturn = joinReplicaSetReturn;
    }

    public JoinReplicaSetResponse() {
        this.joinReplicaSetReturn = new JoinReplicaSetReturn();
    }

    public JoinReplicaSetReturn getJoinReplicaSetReturn() {
        return joinReplicaSetReturn;
    }

    public void setJoinReplicaSetReturn(JoinReplicaSetReturn joinReplicaSetReturn) {
        this.joinReplicaSetReturn = joinReplicaSetReturn;
    }
}
