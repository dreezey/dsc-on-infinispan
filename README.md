# Distributed Session Cache on Infinispan
This project provides an interface between the IBM Security Access Manager (ISAM) WebSEAL / Reverse Proxy DSC client and an Infinispan cluster.

The idea of the project is to provide a scalable alternative, to the otherwise 1 active node cluster ISAM DSC, by leveraging modern technologies.

The project originates from my original Python [proof of concept](https://github.com/dreezey/dsc-on-redis), which allowed the WebSEAL to interface with Redis.

# Technologies
## Quarkus
Quarkus is a Java framework with cloud, microservice and serverless in mind.
Using Quarkus you not only experience live coding (which is very handy), but also the capability of making "native" images, which decrease image footprint, runtime resource usage and decreases startup, a lot.

More information on the Quarkus framework: https://quarkus.io/.

## Infinispan
Infinispan is a distributed key/value data store, like Redis, with native integration in Quarkus. Scaling Infinispan is a fairly easy process and will be described further down.

# Getting started
Before actually getting started, make sure you have a Quarkus environment up and running: https://quarkus.io/get-started/.

## Preparing the service
To run the service, you can use the quarkus plugins for maven, more specifically:
```
./mvnw compile quarkus:dev -Dinfinispan.client.hotrod.server_list=127.0.0.1:11222;
```

This will run Quarkus in development mode, meaning live coding is activated.
The `-Dinfinispan.client.hotrod.server_list=127.0.0.1:11222` serves as an example to override the Infinispan server address.

Since I enabled Basic Authentication on the service, you need to generate a new user, e.g.:

```
WEBSEAL_USER="ispn-dsc-webseald";
MD5SUM="$(echo -n ${WEBSEAL_USER}:dsess:ds3ss | md5 -n)";
cat << EOF | tee -a src/main/resources/dsess-users.properties
${WEBSEAL_USER}=${MD5SUM}
EOF
```

This will add the user and password for the service. If you changed the username, make sure that `src/main/resources/dsess-roles.properties` also contains the new username.

## Infinispan
### First node
Since you probably don't have Infinispan running yet, download it from: https://infinispan.org/download/.
The last version I tested was: `10.1.0.Final`.

Once downloaded, extract the ZIP file and rename the directory:
```
mv infinispan-server-10.1.0.Final infinispan-server-10.1.0.Final-1;
```

And change the `server/conf/infinispan.xml` file:
```
<infinispan
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="urn:infinispan:config:10.1 https://infinispan.org/schemas/infinispan-config-10.1.xsd
                            urn:infinispan:server:10.1 https://infinispan.org/schemas/infinispan-server-10.1.xsd"
        xmlns="urn:infinispan:config:10.1"
        xmlns:server="urn:infinispan:server:10.1">

   <cache-container name="default" statistics="true">
      <transport cluster="${infinispan.cluster.name}" stack="${infinispan.cluster.stack:tcp}"/>
      <distributed-cache name="sessions" mode="SYNC" owners="2">
      	<transaction mode="NONE"/>
      	<state-transfer timeout="60000"/>
      	<indexing index="PRIMARY_OWNER" auto-config="true" />
      </distributed-cache>
      <replicated-cache name="replicas" mode="SYNC">
      	<transaction mode="NONE"/>
      	<state-transfer timeout="60000"/>
      	<indexing index="PRIMARY_OWNER" auto-config="true" />
      </replicated-cache>
      <distributed-cache name="graveyard" mode="SYNC" owners="2">
      	<transaction mode="NONE"/>
      	<state-transfer timeout="60000"/>
      	<indexing index="PRIMARY_OWNER" auto-config="true" />
      </distributed-cache>
   </cache-container>

   <server xmlns="urn:infinispan:server:10.1">
      <interfaces>
         <interface name="public">
            <inet-address value="${infinispan.bind.address:127.0.0.1}"/>
         </interface>
      </interfaces>

      <socket-bindings default-interface="public" port-offset="${infinispan.socket.binding.port-offset:0}">
         <socket-binding name="default" port="${infinispan.bind.port:11222}"/>
      </socket-bindings>

      <security>
         <security-realms>
            <security-realm name="default">
               <!-- Uncomment to enable TLS on the realm -->
               <!-- server-identities>
                  <ssl>
                     <keystore path="application.keystore" relative-to="infinispan.server.config.path"
                               keystore-password="password" alias="server" key-password="password"
                               generate-self-signed-certificate-host="localhost"/>
                  </ssl>
               </server-identities-->
               <properties-realm groups-attribute="Roles">
                  <user-properties path="users.properties" relative-to="infinispan.server.config.path"/>
                  <group-properties path="groups.properties" relative-to="infinispan.server.config.path" />
               </properties-realm>
            </security-realm>
         </security-realms>
      </security>

      <endpoints socket-binding="default" security-realm="default">
         <hotrod-connector name="hotrod"/>
         <rest-connector name="rest"/>
      </endpoints>
   </server>
</infinispan>
```

Once this is done, you can run the server:
```
./bin/server.sh
```

After which Infinispan should start up and create the caches, you can verify this via either the CLI or the web interface.

## Scaling out
Starting a `n-th` node is easy:
```
cp infinispan-server-10.1.0.Final-1 infinispan-server-10.1.0.Final-2;
```

Again, modify the `server/conf/infinispan.xml` and set the `infinispan.socket.binding.port-offset` to 100.

Purge the data directory:
```
rm -rf server/data/*;
```

And start your second node:
```
./bin/server.sh
```

The caches should automatically rebalance.

## IBM Security Access Manager
The final step is to configure the ISAM Reverse Proxy to use the service, include the following configuration:

```
[session]
dsess-enabled = yes
standard-junction-replica-set = ispn

[replica-sets]
replica-set = ispn

[dsess]
dsess-sess-id-pool-size = 125
dsess-cluster-name = ispn

[dsess-cluster:ispn]
server = 9,http://<<YOUR IP>>:8080/DSess
response-by = 60
handle-pool-size = 10
handle-idle-timeout = 30
timeout = 30
max-wait-time = 0
# Important, if you changed the username/password, make sure to edit it here.
basic-auth-user = ispn-dsc-webseald
basic-auth-passwd = ds3ss

ssl-keyfile =
ssl-keyfile-stash =
ssl-keyfile-label =
```

Deploy the changes and restart the reverse proxy, it should now be using Infinispan as DSC backend.

You can now attempt to create and terminate sessions on the reverse proxy, and it should keep working as usual.

# Testing
I provided elaborate testing for the various endpoints, make sure Infinispan is running and execute:
```
mvn clean; # Make sure the protobuf schema is up-to-date
mvn test;
```

# Roadmap
I would like to implement the following features still:
* Kubernetes deployment
* OKD deployment
* Native image building, which currently works, but doesn't run as expected, since I cannot write SOAP responses, which I believe is due to an already open issue: https://github.com/quarkusio/quarkus/issues/4005.
* Better distribution of session data; currently the session has a List\<SessionData\>, which means that if you store the session, all data is stored on a single node, it may be beneficial to instead use indexed session data, and perform queries if needed.
* Review best-practices by Infinispan: https://infinispan.org/docs/stable/titles/tuning/tuning.html#hints_for_program_developers.
* Review session graveyard; when a session is terminated, it should be added to a graveyard and the action "getUpdates" would then pick these up. When I did this however, the WebSEAL started throwing warnings that it couldn't find the session. I'll have to review what exactly this graveyard is supposed to do.
* Use an embedded Infinispan cache for tests.
