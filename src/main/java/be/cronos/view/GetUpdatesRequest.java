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

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getUpdates", namespace = DsessConstants.SMS_NS)
public class GetUpdatesRequest {

    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replica;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String instance;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private String replicaSet;
    @XmlElement(namespace = DsessConstants.SMS_NS)
    private int responseBy;

    public GetUpdatesRequest() {
    }

    public GetUpdatesRequest(String replica, String instance, String replicaSet, int responseBy) {
        this.replica = replica;
        this.instance = instance;
        this.replicaSet = replicaSet;
        this.responseBy = responseBy;
    }

    public String getReplica() {
        return replica;
    }

    public void setReplica(String replica) {
        this.replica = replica;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }

    public int getResponseBy() {
        return responseBy;
    }

    public void setResponseBy(int responseBy) {
        this.responseBy = responseBy;
    }

    @Override
    public String toString() {
        return "GetUpdatesRequest{" +
                "replica='" + replica + '\'' +
                ", instance='" + instance + '\'' +
                ", replicaSet='" + replicaSet + '\'' +
                ", responseBy=" + responseBy +
                '}';
    }
}
