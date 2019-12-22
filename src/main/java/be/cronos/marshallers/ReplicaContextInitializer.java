package be.cronos.marshallers;

import be.cronos.model.ispn.Replica;
import be.cronos.model.ispn.ReplicaSet;
import be.cronos.model.ispn.Session;
import be.cronos.model.ispn.SessionData;
import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

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
