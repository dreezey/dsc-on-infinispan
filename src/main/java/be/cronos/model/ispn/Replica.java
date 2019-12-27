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

import java.util.Objects;

public class Replica {
    private String replica;
    private String instance;
    private int capabilities;
    private String replicaSet;

    @ProtoFactory
    public Replica(String replica, String instance, int capabilities, String replicaSet) {
        this.replica = Objects.requireNonNull(replica);
        this.instance = Objects.requireNonNull(instance);
        this.capabilities = capabilities;
        this.replicaSet = Objects.requireNonNull(replicaSet);
    }

    @ProtoField(number = 1, required = true)
    public String getReplica() {
        return replica;
    }

    public void setReplica(String replica) {
        this.replica = replica;
    }

    @ProtoField(number = 2, required = true)
    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    @ProtoField(number = 3, defaultValue = "15")
    public int getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(int capabilities) {
        this.capabilities = capabilities;
    }

    @ProtoField(number = 4, required = true)
    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }

    @Override
    public String toString() {
        return "Replica{" +
                "replica='" + replica + '\'' +
                ", instance='" + instance + '\'' +
                ", capabilities=" + capabilities +
                ", replicaSet='" + replicaSet + '\'' +
                '}';
    }
}
