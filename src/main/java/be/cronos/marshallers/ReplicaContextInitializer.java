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

package be.cronos.marshallers;

import be.cronos.model.ispn.Replica;
import be.cronos.model.ispn.Session;
import be.cronos.model.ispn.SessionData;
import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

/**
 * @author <a href="mailto:dries.eestermans@is4u.be">Dries Eestermans</a>
 */
@AutoProtoSchemaBuilder(
        includeClasses = {
                Replica.class,
                Session.class,
                SessionData.class
        },
        schemaPackageName = "isam_dsess"
)
public interface ReplicaContextInitializer extends SerializationContextInitializer {
}
