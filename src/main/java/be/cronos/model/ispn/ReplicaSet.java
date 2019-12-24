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

import java.util.*;

public class ReplicaSet {
    private String name;
    private ArrayList<String> replicas;
//    private HashMap<String, Replica> replicas;


    public ReplicaSet(String name) {
        this.name = name;
        this.replicas = new ArrayList<>();
//        this.replicas = new HashMap<>();
//        this.sessions = new HashSet<>();
    }

    @ProtoFactory
    public ReplicaSet(String name, ArrayList<String> replicas/*, HashMap<String, Replica> replicas*/) {
        this.name = Objects.requireNonNull(name);
        this.replicas = Objects.requireNonNull(replicas);
//        this.replicas = replicas;
//        this.sessions = sessions;
    }

    @ProtoField(number = 1, defaultValue = "default")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ProtoField(number = 2)
    public ArrayList<String> getReplicas() {
        return replicas;
    }

    public void setReplicas(ArrayList<String> replicas) {
        this.replicas = replicas;
    }

    //    @ProtoField(number = 2)
//    public HashMap<String, Replica> getReplicas() {
//        return replicas;
//    }
//
//    public void setReplicas(HashMap<String, Replica> replicas) {
//        this.replicas = replicas;
//    }

//    @ProtoField(number = 2)
//    public Set<String> getSessions() {
//        return sessions;
//    }
//
//    public void setSessions(Set<String> sessions) {
//        this.sessions = sessions;
//    }
}
