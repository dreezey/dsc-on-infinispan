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

package be.cronos;

import be.cronos.model.*;
import be.cronos.model.ispn.SessionData;
import be.cronos.util.XmlTestUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DSessResourceTest {

    private static final ArrayList<Header> basicHeaders = new ArrayList<Header>() {{
        add(new Header("Content-Type", "text/xml"));
        add(new Header("Accept", "text/xml"));
    }};

    private final ArrayList<SessionData> sessionData = new ArrayList<SessionData>() {{
        add(new SessionData("test1", "value1", "sms", 1));
        add(new SessionData("test2", "value2", "sms", 1));
        add(new SessionData("test3", "value3", "sms", 0));
    }};

    private static final String BA_USER = "ispn-dsc-webseald";
    private static final String BA_PASSWORD = "ds3ss";

    private static final String REPLICA = "test-webseald";
    private static final String REPLICA_SET = "dsc-test";
    private static final String INSTANCE = "e1cbcdd6-d46b-11e9-9e4d-000c2974ca3a:test-webseald";

    @Test
    public void testHealthEndpoint() {
        given()
          .when().get("/DSess/health")
          .then()
             .statusCode(200)
             .body(is("healthy"));
    }

    @Test
    public void testPingEndpoint() {
        // Append the SOAP action
        Headers headers = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "ping");
        // Stringify the SOAP Request Envelope
        String pingRequestSoapMessage = XmlTestUtils.GetSoapMessage(
                new PingRequest(DSCResultCode.OK.getResultCode()),
                PingRequest.class
        );
        // Stringify the SOAP Response Envelope
        String pingResponseSoapMessage = XmlTestUtils.GetSoapMessage(
                new PingResponse(DSCResultCode.OK.getResultCode()),
                PingResponse.class
        );
        // Do the tests
        given()
                .when()
                .headers(headers)
                .body(pingRequestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(pingResponseSoapMessage));
        given()
                .when()
                .headers(headers)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(pingRequestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(pingResponseSoapMessage));
        given()
                .when()
                .headers(headers)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body("")
                .post("/DSess")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetUpdatesEndpoint() {
        // Append the SOAP action
        Headers headers = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "getUpdates");
        // Stringify the SOAP Request Envelope
        int responseBy = 1;
        String getUpdatesRequestSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetUpdatesRequest(
                        REPLICA,
                        INSTANCE,
                        REPLICA_SET,
                        responseBy
                ),
                GetUpdatesRequest.class
        );
        // Stringify the SOAP Response Envelope
        String getUpdatesResponseSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetUpdatesResponse(
                        new GetUpdatesReturn(
                                DSCResultCode.OK.getResultCode(),
                                DsessConstants.NEW_KEY,
                                0
                        )
                ),
                GetUpdatesResponse.class
        );
        // Do the tests
        given()
                .when()
                .headers(headers)
                .body(getUpdatesRequestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(401);
        given()
                .when()
                .headers(headers)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(getUpdatesRequestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(getUpdatesResponseSoapMessage))
                .time(is(Long.parseLong(String.valueOf(responseBy))), TimeUnit.SECONDS);
        given()
                .when()
                .headers(headers)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body("")
                .post("/DSess")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(1)
    public void testJoinReplicaSetEndpoint() {
        // Append the SOAP action
        Headers headers = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "joinReplicaSet");
        // Stringify the SOAP Request Envelope
        String joinReplicaSetRequestSoapMessage = XmlTestUtils.GetSoapMessage(
                new JoinReplicaSetRequest(
                        REPLICA,
                        INSTANCE,
                        15,
                        REPLICA_SET
                ),
                JoinReplicaSetRequest.class
        );
        // Stringify the SOAP Response Envelope
        String joinReplicaSetResponseSoapMessage = XmlTestUtils.GetSoapMessage(
                new JoinReplicaSetResponse(
                        new JoinReplicaSetReturn(
                                DSCResultCode.OK.getResultCode(),
                                0,
                                0,
                                -1
                        )
                ),
                JoinReplicaSetResponse.class
        );
        // Do the tests
        given()
                .when()
                .headers(headers)
                .body(joinReplicaSetRequestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(401);
        given()
                .when()
                .headers(headers)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(joinReplicaSetRequestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(joinReplicaSetResponseSoapMessage));
        given()
                .when()
                .headers(headers)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body("")
                .post("/DSess")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(2)
    public void testReplicaShutdownEndpoint() {
        // Append the SOAP action
        Headers headers = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "replicaShutdown");
        // Stringify the SOAP Request Envelope
        String requestSoapMessage = XmlTestUtils.GetSoapMessage(
                new ReplicaShutdownRequest(
                        REPLICA
                ),
                ReplicaShutdownRequest.class
        );
        // Stringify the SOAP Response Envelope
        String responseSoapMessage = XmlTestUtils.GetSoapMessage(
                new ReplicaShutdownResponse(
                        DSCResultCode.OK.getResultCode()
                ),
                ReplicaShutdownResponse.class
        );
        // Do the tests
        given()
                .when()
                .headers(headers)
                .body(requestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(401);
        given()
                .when()
                .headers(headers)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseSoapMessage));
        given()
                .when()
                .headers(headers)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body("")
                .post("/DSess")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetRealmNameEndpoint() {
        // Append the SOAP action
        Headers headers = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "getRealmName");
        // Stringify the SOAP Request Envelope
        String requestSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetRealmNameRequest(
                        REPLICA,
                        REPLICA_SET
                ),
                GetRealmNameRequest.class
        );
        // Stringify the SOAP Response Envelope
        String responseSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetRealmNameResponse(
                        new GetRealmNameReturn(
                                DSCResultCode.OK.getResultCode(),
                                DsessConstants.REALM_NAME
                        )
                ),
                GetRealmNameResponse.class
        );
        // Do the tests
        given()
                .when()
                .headers(headers)
                .body(requestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(401);
        given()
                .when()
                .headers(headers)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseSoapMessage));
        given()
                .when()
                .headers(headers)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body("")
                .post("/DSess")
                .then()
                .statusCode(400);
    }

    @Test
//    @Order(3)
    public void testCreateSessionEndpoint() {
        // When a session is created, the result code should be: 952467756.
        // When a session with that ID already exists: 952467761.
        // Session request without data also generates: 952467756.
        // Session request with a replica set that does not exist: 952467762.
        // Session request without a session ID: 952467756. (still had data in request -> session also created without ID)
        // Session request without a session ID and no data: 952467761.

        String sessionId = "TEST_SESSION_ID_123456789";
        List<SessionDataRequest> sessionDataRequestList = new ArrayList<>();
        sessionData.forEach(sessionData1 -> {
            sessionDataRequestList.add(
                    new SessionDataRequest(
                            sessionData1.getDataClass(),
                            sessionData1.getValue(),
                            sessionData1.getInstance(),
                            sessionData1.getChangePolicy()
                    )
            );
        });

        // Append the SOAP action
        Headers createSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "createSession");
        Headers joinReplicaSet = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "joinReplicaSet");
        Headers replicaShutdown = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "replicaShutdown");
        Headers terminateSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "terminateSession");

        // Custom replica settings
        String CREATE_SESSION_REPLICA = "create-session-webseald";
        String CREATE_SESSION_REPLICA_SET = "create-session-dsc";
        String CREATE_SESSION_INSTANCE = "e1cbcdd6-d46b-11e9-9e4d-000c2974ca3a:create-session-webseald";

        // Stringify the SOAP Request Envelope
        String requestCreateSessionNoIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new CreateSessionRequest(
                        CREATE_SESSION_REPLICA,
                        CREATE_SESSION_REPLICA_SET,
                        null,
                        0,
                        sessionDataRequestList
                ),
                CreateSessionRequest.class
        );
        String requestCreateSessionNoDataSoapMessage = XmlTestUtils.GetSoapMessage(
                new CreateSessionRequest(
                        CREATE_SESSION_REPLICA,
                        CREATE_SESSION_REPLICA_SET,
                        sessionId,
                        0,
                        null
                ),
                CreateSessionRequest.class
        );
        String requestSoapMessage = XmlTestUtils.GetSoapMessage(
                new CreateSessionRequest(
                        CREATE_SESSION_REPLICA,
                        CREATE_SESSION_REPLICA_SET,
                        sessionId,
                        0,
                        sessionDataRequestList
                ),
                CreateSessionRequest.class
        );
        // Stringify the SOAP Response Envelope
        String responseOKSoapMessage = XmlTestUtils.GetSoapMessage(
                CreateSessionResponse.constructCreateSessionResponse(DSCResultCode.OK.getResultCode()),
                CreateSessionResponse.class
        );
        String responseNotCreatedSoapMessage = XmlTestUtils.GetSoapMessage(
                CreateSessionResponse.constructCreateSessionResponse(DSCResultCode.NOT_CREATED.getResultCode()),
                CreateSessionResponse.class
        );
        String responseNoReplicaSetSoapMessage = XmlTestUtils.GetSoapMessage(
                CreateSessionResponse.constructCreateSessionResponse(DSCResultCode.REPLICA_SET_NOT_FOUND.getResultCode()),
                CreateSessionResponse.class
        );
        // Do the tests
        given()
                .when()
                .headers(createSession)
                .body(requestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(401);
        given()
                .when()
                .headers(createSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body("")
                .post("/DSess")
                .then()
                .statusCode(400);
        // Attempt to create a session without ID
        given()
                .when()
                .headers(createSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestCreateSessionNoIdSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseNotCreatedSoapMessage));
        // Attempt to create a session without data
        given()
                .when()
                .headers(createSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestCreateSessionNoDataSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseNotCreatedSoapMessage));
        // No replica set is created at this point, the service should tell us
        given()
                .when()
                .headers(createSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseNoReplicaSetSoapMessage));
        // Now attempt to get good response, which means we need to create the replica set first
        given()
                .when()
                .headers(joinReplicaSet)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new JoinReplicaSetRequest(CREATE_SESSION_REPLICA, CREATE_SESSION_INSTANCE, 15, CREATE_SESSION_REPLICA_SET),
                        JoinReplicaSetRequest.class
                    )
                )
                .post("/DSess")
                .then()
                .statusCode(200);
        given()
                .when()
                .headers(createSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseOKSoapMessage));
        // Since the session ID already exists, it should not be created
        given()
                .when()
                .headers(createSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseNotCreatedSoapMessage));
        // clean the session up
        given()
                .when()
                .headers(terminateSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new TerminateSessionRequest(
                                CREATE_SESSION_REPLICA,
                                CREATE_SESSION_REPLICA_SET,
                                sessionId,
                                0,
                                0,
                                0
                        ),
                        TerminateSessionRequest.class
                    )
                )
                .post("/DSess")
                .then()
                .statusCode(200);
        // And shut down the replica
        given()
                .when()
                .headers(replicaShutdown)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new ReplicaShutdownRequest(CREATE_SESSION_REPLICA),
                        ReplicaShutdownRequest.class
                        )
                )
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(
                        XmlTestUtils.GetSoapMessage(
                                new ReplicaShutdownResponse(
                                        DSCResultCode.OK.getResultCode()
                                ),
                                ReplicaShutdownResponse.class
                        )
                    )
                );
    }

    @Test
    public void testChangeSessionEndpoint() {
        // When a session is modified, the result code should be: 952467756.
        // When a modification has no target session, it generates: 952467768.
        // When changing a value previously with changePolicy on '1', it generates: 952467756.
        // Changing a session with an invalid version result in: 952467788.
        // When providing a wrong replica set: 952467762.
        String sessionId = "TEST_CHANGE_SESSION_ID_123456789";
        List<SessionDataRequest> createSessionData = new ArrayList<>();
        sessionData.forEach(sessionData1 -> {
            createSessionData.add(
                    new SessionDataRequest(
                            sessionData1.getDataClass(),
                            sessionData1.getValue(),
                            sessionData1.getInstance(),
                            sessionData1.getChangePolicy()
                    )
            );
        });

        List<SessionDataRequest> changeSessionDataList = new ArrayList<SessionDataRequest>() {{
            add(new SessionDataRequest("change.session", "yes", "sms", 0));
        }};

        // Append the SOAP action
        Headers createSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "createSession");
        Headers changeSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "changeSession");
        Headers joinReplicaSet = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "joinReplicaSet");
        Headers replicaShutdown = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "replicaShutdown");
        Headers terminateSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "terminateSession");

        // Custom replica settings
        String CHANGE_SESSION_REPLICA = "change-session-webseald";
        String CHANGE_SESSION_REPLICA_SET = "change-session-dsc";
        String CHANGE_SESSION_INSTANCE = "e1cbcdd6-d46b-11e9-9e4d-000c2974ca3a:change-session-webseald";

        // Stringify the SOAP Request Envelope
        String requestCreateSessionSoapMessage = XmlTestUtils.GetSoapMessage(
                new CreateSessionRequest(
                        CHANGE_SESSION_REPLICA,
                        CHANGE_SESSION_REPLICA_SET,
                        sessionId,
                        0,
                        createSessionData
                ),
                CreateSessionRequest.class
        );
        String requestChangeSessionInvalidReplicaSetSoapMessage = XmlTestUtils.GetSoapMessage(
                new ChangeSessionRequest(
                        CHANGE_SESSION_REPLICA,
                        "invalid-replicaset",
                        sessionId,
                        0,
                        false,
                        0,
                        changeSessionDataList
                ),
                ChangeSessionRequest.class
        );
        String requestChangeSessionSoapMessage = XmlTestUtils.GetSoapMessage(
                new ChangeSessionRequest(
                        CHANGE_SESSION_REPLICA,
                        CHANGE_SESSION_REPLICA_SET,
                        sessionId,
                        0,
                        false,
                        0,
                        changeSessionDataList
                ),
                ChangeSessionRequest.class
        );
        String requestChangeSessionNoIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new ChangeSessionRequest(
                        CHANGE_SESSION_REPLICA,
                        CHANGE_SESSION_REPLICA_SET,
                        null,
                        0,
                        false,
                        0,
                        changeSessionDataList
                ),
                ChangeSessionRequest.class
        );
        String requestChangeSessionNoDataSoapMessage = XmlTestUtils.GetSoapMessage(
                new ChangeSessionRequest(
                        CHANGE_SESSION_REPLICA,
                        CHANGE_SESSION_REPLICA_SET,
                        sessionId,
                        0,
                        false,
                        0,
                        null
                ),
                ChangeSessionRequest.class
        );
        String requestChangeSessionWrongReplicaSetSoapMessage = XmlTestUtils.GetSoapMessage(
                new ChangeSessionRequest(
                        CHANGE_SESSION_REPLICA,
                        "doesntexist",
                        sessionId,
                        0,
                        false,
                        0,
                        changeSessionDataList
                ),
                ChangeSessionRequest.class
        );

        // Stringify the SOAP Response Envelopes
        String responseCreateSessionOkSoapMessage = XmlTestUtils.GetSoapMessage(
                CreateSessionResponse.constructCreateSessionResponse(DSCResultCode.OK.getResultCode()),
                CreateSessionResponse.class
        );
        String responseChangeSessionOkSoapMessage = XmlTestUtils.GetSoapMessage(
                new ChangeSessionResponse(
                        new ChangeSessionReturn(
                                DSCResultCode.OK.getResultCode(),
                                1,
                                1,
                                false
                        )
                ),
                ChangeSessionResponse.class
        );
        String responseChangeSessionNotChangedSoapMessage = XmlTestUtils.GetSoapMessage(
                new ChangeSessionResponse(
                        new ChangeSessionReturn(
                                DSCResultCode.NOT_CHANGED.getResultCode(),
                                0,
                                1,
                                false
                        )
                ),
                ChangeSessionResponse.class
        );
        String responseChangeSessionInvalidReplicaSetSoapMessage = XmlTestUtils.GetSoapMessage(
                new ChangeSessionResponse(
                        new ChangeSessionReturn(
                                DSCResultCode.REPLICA_SET_NOT_FOUND.getResultCode(),
                                0,
                                1,
                                false
                        )
                ),
                ChangeSessionResponse.class
        );


        // Do the tests
        given()
                .when()
                .headers(changeSession)
                .body(requestChangeSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(401);
        given()
                .when()
                .headers(changeSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body("")
                .post("/DSess")
                .then()
                .statusCode(400);
        // Attempt to change a session without session ID
        given()
                .when()
                .headers(changeSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestChangeSessionNoIdSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseChangeSessionNotChangedSoapMessage));
        // Attempt to change a session without data
        given()
                .when()
                .headers(changeSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestChangeSessionNoDataSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseChangeSessionNotChangedSoapMessage));
        // Attempt to change a session but with a wrong replica set
        given()
                .when()
                .headers(changeSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestChangeSessionInvalidReplicaSetSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseChangeSessionInvalidReplicaSetSoapMessage));
        // Now attempt to get good response, which means we need to create the replica set first
        given()
                .when()
                .headers(joinReplicaSet)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new JoinReplicaSetRequest(
                                CHANGE_SESSION_REPLICA,
                                CHANGE_SESSION_INSTANCE,
                                15,
                                CHANGE_SESSION_REPLICA_SET
                        ),
                        JoinReplicaSetRequest.class
                    )
                )
                .post("/DSess")
                .then()
                .statusCode(200);
        // Now create the session which we'll be modifying
        given()
                .when()
                .headers(createSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestCreateSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseCreateSessionOkSoapMessage));
        given()
                .when()
                .headers(changeSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestChangeSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseChangeSessionOkSoapMessage));
        // clean the session up
        given()
                .when()
                .headers(terminateSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new TerminateSessionRequest(
                                CHANGE_SESSION_REPLICA,
                                CHANGE_SESSION_REPLICA_SET,
                                sessionId,
                                0,
                                0,
                                0
                        ),
                        TerminateSessionRequest.class
                        )
                )
                .post("/DSess")
                .then()
                .statusCode(200);
        // And shut down the replica
        given()
                .when()
                .headers(replicaShutdown)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new ReplicaShutdownRequest(CHANGE_SESSION_REPLICA),
                        ReplicaShutdownRequest.class
                        )
                )
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(XmlTestUtils.GetSoapMessage(
                                new ReplicaShutdownResponse(
                                        DSCResultCode.OK.getResultCode()
                                ),
                                ReplicaShutdownResponse.class
                        )
                    )
                );
    }

    @Test
    public void testTerminateSessionEndpoint() {
        // Terminate a session which does not exist: 952467768.
        // Terminate a session, but wrong replica set: 952467762.
        // Terminate a session, success: 952467756. (also removes the session from DSC)
        //      It now provides this via "getUpdates" as well: <ns1:terminations> <ns1:replicaSet>dsc-test</ns1:replicaSet><ns1:sessionID>LXoBcBXkyDhKqfirUpZy12vgNlPm2sW17zKIXnNCxNVKuFZxy2o=</ns1:sessionID> </ns1:terminations>
        // Terminate a session, bogus reason: 952467756.
        String sessionId = "TEST_TERMINATE_SESSION_ID_123456789";
        List<SessionDataRequest> createSessionData = new ArrayList<>();
        sessionData.forEach(sessionData1 -> {
            createSessionData.add(
                    new SessionDataRequest(
                            sessionData1.getDataClass(),
                            sessionData1.getValue(),
                            sessionData1.getInstance(),
                            sessionData1.getChangePolicy()
                    )
            );
        });

        // Append the SOAP action
        Headers createSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "createSession");
        Headers joinReplicaSet = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "joinReplicaSet");
        Headers replicaShutdown = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "replicaShutdown");
        Headers terminateSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "terminateSession");

        // Custom replica settings
        String TERMINATE_SESSION_REPLICA = "terminate-session-webseald";
        String TERMINATE_SESSION_REPLICA_SET = "terminate-session-dsc";
        String TERMINATE_SESSION_INSTANCE = "e1cbcdd6-d46b-11e9-9e4d-000c2974ca3a:terminate-session-webseald";

        // Stringify the SOAP Request Envelope
        String requestCreateSessionSoapMessage = XmlTestUtils.GetSoapMessage(
                new CreateSessionRequest(
                        TERMINATE_SESSION_REPLICA,
                        TERMINATE_SESSION_REPLICA_SET,
                        sessionId,
                        0,
                        createSessionData
                ),
                CreateSessionRequest.class
        );
        String requestTerminateSessionSoapMessage = XmlTestUtils.GetSoapMessage(
                new TerminateSessionRequest(
                        TERMINATE_SESSION_REPLICA,
                        TERMINATE_SESSION_REPLICA_SET,
                        sessionId,
                        0,
                        0,
                        0
                ),
                TerminateSessionRequest.class
        );
        String requestTerminateSessionNullSessionSoapMessage = XmlTestUtils.GetSoapMessage(
                new TerminateSessionRequest(
                        TERMINATE_SESSION_REPLICA,
                        TERMINATE_SESSION_REPLICA_SET,
                        null,
                        0,
                        0,
                        0
                ),
                TerminateSessionRequest.class
        );
        String requestTerminateSessionInvalidIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new TerminateSessionRequest(
                        TERMINATE_SESSION_REPLICA,
                        TERMINATE_SESSION_REPLICA_SET,
                        "terminatemenot",
                        0,
                        0,
                        0
                ),
                TerminateSessionRequest.class
        );
        String requestTerminateSessionInvalidReplicaSetSoapMessage = XmlTestUtils.GetSoapMessage(
                new TerminateSessionRequest(
                        TERMINATE_SESSION_REPLICA,
                        "terminate-dsc-wrong",
                        sessionId,
                        0,
                        0,
                        0
                ),
                TerminateSessionRequest.class
        );

        // Stringify the SOAP Response Envelopes
        String responseCreateSessionOkSoapMessage = XmlTestUtils.GetSoapMessage(
                CreateSessionResponse.constructCreateSessionResponse(DSCResultCode.OK.getResultCode()),
                CreateSessionResponse.class
        );
        String responseTerminateSessionInvalidIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new TerminateSessionResponse(
                        new TerminateSessionReturn(
                                DSCResultCode.NOT_CHANGED.getResultCode(),
                                0,
                                1,
                                false
                        )
                ),
                TerminateSessionResponse.class
        );
        String responseTerminateSessionInvalidReplicaSetSoapMessage = XmlTestUtils.GetSoapMessage(
                new TerminateSessionResponse(
                        new TerminateSessionReturn(
                                DSCResultCode.REPLICA_SET_NOT_FOUND.getResultCode(),
                                0,
                                1,
                                false
                        )
                ),
                TerminateSessionResponse.class
        );
        String responseTerminateSessionOkSoapMessage = XmlTestUtils.GetSoapMessage(
                new TerminateSessionResponse(
                        new TerminateSessionReturn(
                                DSCResultCode.OK.getResultCode(),
                                0,
                                1,
                                false
                        )
                ),
                TerminateSessionResponse.class
        );

        // Do the tests
        given()
                .when()
                .headers(terminateSession)
                .body(requestTerminateSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(401);
        given()
                .when()
                .headers(terminateSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body("")
                .post("/DSess")
                .then()
                .statusCode(400);
        // Attempt to terminate a session without session ID
        given()
                .when()
                .headers(terminateSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestTerminateSessionNullSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseTerminateSessionInvalidIdSoapMessage));
        // Now create the session
        given()
                .when()
                .headers(joinReplicaSet)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new JoinReplicaSetRequest(
                                TERMINATE_SESSION_REPLICA,
                                TERMINATE_SESSION_INSTANCE,
                                15,
                                TERMINATE_SESSION_REPLICA_SET
                        ),
                        JoinReplicaSetRequest.class
                        )
                )
                .post("/DSess")
                .then()
                .statusCode(200);
        // Now create the session which we'll be modifying
        given()
                .when()
                .headers(createSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestCreateSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseCreateSessionOkSoapMessage));
        // Attempt to terminate a session with an invalid session id
        given()
                .when()
                .headers(terminateSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestTerminateSessionInvalidIdSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseTerminateSessionInvalidIdSoapMessage));
        // Attempt to terminate a session with an invalid replica set
        given()
                .when()
                .headers(terminateSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestTerminateSessionInvalidReplicaSetSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseTerminateSessionInvalidReplicaSetSoapMessage));
        // Now terminate a session which should be success
        given()
                .when()
                .headers(terminateSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestTerminateSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseTerminateSessionOkSoapMessage));
        // And shut down the replica
        given()
                .when()
                .headers(replicaShutdown)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new ReplicaShutdownRequest(TERMINATE_SESSION_REPLICA),
                        ReplicaShutdownRequest.class
                        )
                )
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(XmlTestUtils.GetSoapMessage(
                        new ReplicaShutdownResponse(
                                DSCResultCode.OK.getResultCode()
                        ),
                        ReplicaShutdownResponse.class
                        )
                    )
                );
    }

    @Test
    public void testIdleTimeoutEndpoint() {
        // Idle a session which does not exist: 952467768.
        // Idle a session, but wrong replica set: 952467762.
        // Idle a session, success: 952467756.
        //      Now provided via "getUpdates"? --> no
        //      "getSession" now returns: 952467768 --> session no longer exists. (removed from DSC)

        String sessionId = "TEST_IDLE_SESSION_ID_123456789";
        List<SessionDataRequest> createSessionData = new ArrayList<>();
        sessionData.forEach(sessionData1 -> {
            createSessionData.add(
                    new SessionDataRequest(
                            sessionData1.getDataClass(),
                            sessionData1.getValue(),
                            sessionData1.getInstance(),
                            sessionData1.getChangePolicy()
                    )
            );
        });

        // Append the SOAP action
        Headers createSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "createSession");
        Headers joinReplicaSet = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "joinReplicaSet");
        Headers replicaShutdown = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "replicaShutdown");
        Headers idleTimeout = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "idleTimeout");

        // Custom replica settings
        String IDLE_TIMEOUT_REPLICA = "idle-timeout-webseald";
        String IDLE_TIMEOUT_REPLICA_SET = "idle-timeout-dsc";
        String IDLE_TIMEOUT_INSTANCE = "e1cbcdd6-d46b-11e9-9e4d-000c2974ca3a:idle-timeout-webseald";

        // Stringify the SOAP Request Envelope
        String requestCreateSessionSoapMessage = XmlTestUtils.GetSoapMessage(
                new CreateSessionRequest(
                        IDLE_TIMEOUT_REPLICA,
                        IDLE_TIMEOUT_REPLICA_SET,
                        sessionId,
                        0,
                        createSessionData
                ),
                CreateSessionRequest.class
        );
        String requestIdleTimeoutSoapMessage = XmlTestUtils.GetSoapMessage(
                new IdleTimeoutRequest(
                        IDLE_TIMEOUT_REPLICA,
                        IDLE_TIMEOUT_REPLICA_SET,
                        sessionId
                ),
                IdleTimeoutRequest.class
        );
        String requestIdleTimeoutNullSessionIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new IdleTimeoutRequest(
                        IDLE_TIMEOUT_REPLICA,
                        IDLE_TIMEOUT_REPLICA_SET,
                        null
                ),
                IdleTimeoutRequest.class
        );
        String requestIdleTimeoutInvalidIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new IdleTimeoutRequest(
                        IDLE_TIMEOUT_REPLICA,
                        IDLE_TIMEOUT_REPLICA_SET,
                        "terminatemenot"
                ),
                IdleTimeoutRequest.class
        );
        String requestIdleTimeoutInvalidReplicaSetSoapMessage = XmlTestUtils.GetSoapMessage(
                new IdleTimeoutRequest(
                        IDLE_TIMEOUT_REPLICA,
                        "terminate-dsc-wrong",
                        sessionId
                ),
                IdleTimeoutRequest.class
        );
        // Stringify the SOAP Response Envelopes
        String responseCreateSessionOkSoapMessage = XmlTestUtils.GetSoapMessage(
                CreateSessionResponse.constructCreateSessionResponse(DSCResultCode.OK.getResultCode()),
                CreateSessionResponse.class
        );
        String responseIdleTimeoutInvalidIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new IdleTimeoutResponse(DSCResultCode.NOT_CHANGED.getResultCode()),
                IdleTimeoutResponse.class
        );
        String responseIdleTimeoutInvalidReplicaSetSoapMessage = XmlTestUtils.GetSoapMessage(
                new IdleTimeoutResponse(DSCResultCode.REPLICA_SET_NOT_FOUND.getResultCode()),
                IdleTimeoutResponse.class
        );
        String responseIdleTimeoutOkSoapMessage = XmlTestUtils.GetSoapMessage(
                new IdleTimeoutResponse(DSCResultCode.OK.getResultCode()),
                IdleTimeoutResponse.class
        );

        // Do the tests
        given()
                .when()
                .headers(idleTimeout)
                .body(requestIdleTimeoutSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(401);
        given()
                .when()
                .headers(idleTimeout)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body("")
                .post("/DSess")
                .then()
                .statusCode(400);
        // Attempt to idle a session without session ID
        given()
                .when()
                .headers(idleTimeout)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestIdleTimeoutNullSessionIdSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseIdleTimeoutInvalidIdSoapMessage));
        // Now create the session
        given()
                .when()
                .headers(joinReplicaSet)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new JoinReplicaSetRequest(
                                IDLE_TIMEOUT_REPLICA,
                                IDLE_TIMEOUT_INSTANCE,
                                15,
                                IDLE_TIMEOUT_REPLICA_SET
                        ),
                        JoinReplicaSetRequest.class
                        )
                )
                .post("/DSess")
                .then()
                .statusCode(200);
        // Now create the session which we'll be modifying
        given()
                .when()
                .headers(createSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestCreateSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseCreateSessionOkSoapMessage));
        // Attempt to idle a session with an invalid session id
        given()
                .when()
                .headers(idleTimeout)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestIdleTimeoutInvalidIdSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseIdleTimeoutInvalidIdSoapMessage));
        // Attempt to idle a session with an invalid replica set
        given()
                .when()
                .headers(idleTimeout)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestIdleTimeoutInvalidReplicaSetSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseIdleTimeoutInvalidReplicaSetSoapMessage));
        // Now idle a session which should be success
        given()
                .when()
                .headers(idleTimeout)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestIdleTimeoutSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseIdleTimeoutOkSoapMessage));
        // And shut down the replica
        given()
                .when()
                .headers(replicaShutdown)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new ReplicaShutdownRequest(IDLE_TIMEOUT_REPLICA),
                        ReplicaShutdownRequest.class
                        )
                )
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(XmlTestUtils.GetSoapMessage(
                        new ReplicaShutdownResponse(
                                DSCResultCode.OK.getResultCode()
                        ),
                        ReplicaShutdownResponse.class
                        )
                    )
                );
    }

    @Test
    public void testGetSessionEndpoint() {
        // Get a session which does not exist: 952467768.
        // Get a session, invalid replica set: 952467762.
        // Get a session with empty session ID: 952467768.
        // Get a session with null session ID: 952467768.
        // Get a session which exists: 952467756.

        String sessionId = "TEST_GET_SESSION_ID_123456789";
        List<SessionDataRequest> createSessionData = new ArrayList<>();
        ArrayList<GetSessionDataReturn> getSessionDataReturns = new ArrayList<>();
        sessionData.forEach(sessionData1 -> {
            // Add data for create request
            createSessionData.add(
                    new SessionDataRequest(
                            sessionData1.getDataClass(),
                            sessionData1.getValue(),
                            sessionData1.getInstance(),
                            sessionData1.getChangePolicy()
                    )
            );
            // Add data for get request
            getSessionDataReturns.add(
                    new GetSessionDataReturn(
                            sessionData1.getDataClass(),
                            sessionData1.getValue(),
                            sessionData1.getInstance(),
                            sessionData1.getChangePolicy()
                    )
            );
        });

        // Append the SOAP action
        Headers createSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "createSession");
        Headers joinReplicaSet = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "joinReplicaSet");
        Headers replicaShutdown = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "replicaShutdown");
        Headers getSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "getSession");
        Headers terminateSession = XmlTestUtils.getSoapRequestHeaders(basicHeaders, "terminateSession");

        // Custom replica settings
        String GET_SESSION_REPLICA = "get-session-webseald";
        String GET_SESSION_REPLICA_SET = "get-session-dsc";
        String GET_SESSION_INSTANCE = "e1cbcdd6-d46b-11e9-9e4d-000c2974ca3a:get-session-webseald";

        // Stringify the SOAP Request Envelope
        String requestCreateSessionSoapMessage = XmlTestUtils.GetSoapMessage(
                new CreateSessionRequest(
                        GET_SESSION_REPLICA,
                        GET_SESSION_REPLICA_SET,
                        sessionId,
                        0,
                        createSessionData
                ),
                CreateSessionRequest.class
        );
        String requestGetSessionSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetSessionRequest(
                        GET_SESSION_REPLICA,
                        GET_SESSION_REPLICA_SET,
                        sessionId,
                        "local",
                        null
                ),
                GetSessionRequest.class
        );
        String requestGetSessionNullSessionIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetSessionRequest(
                        GET_SESSION_REPLICA,
                        GET_SESSION_REPLICA_SET,
                        null,
                        "local",
                        null
                ),
                GetSessionRequest.class
        );
        String requestGetSessionInvalidIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetSessionRequest(
                        GET_SESSION_REPLICA,
                        GET_SESSION_REPLICA_SET,
                        "getid-notexist",
                        "local",
                        null
                ),
                GetSessionRequest.class
        );
        String requestGetSessionInvalidReplicaSetSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetSessionRequest(
                        GET_SESSION_REPLICA,
                        "get-session-notexist-dsc",
                        sessionId,
                        "local",
                        null
                ),
                GetSessionRequest.class
        );
        String requestTerminateSessionSoapMessage = XmlTestUtils.GetSoapMessage(
                new TerminateSessionRequest(
                        GET_SESSION_REPLICA,
                        GET_SESSION_REPLICA_SET,
                        sessionId,
                        0,
                        0,
                        0
                ),
                TerminateSessionRequest.class
        );

        // Stringify the SOAP Response Envelopes
        String responseCreateSessionOkSoapMessage = XmlTestUtils.GetSoapMessage(
                CreateSessionResponse.constructCreateSessionResponse(DSCResultCode.OK.getResultCode()),
                CreateSessionResponse.class
        );
        String responseGetSessionNullSessionidIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetSessionResponse(
                        new GetSessionReturn(
                                DSCResultCode.NOT_CHANGED.getResultCode(),
                                0,
                                1,
                                null
                        )
                ),
                GetSessionResponse.class
        );
        String responseGetSessionInvalidIdSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetSessionResponse(
                        new GetSessionReturn(
                                DSCResultCode.NOT_CHANGED.getResultCode(),
                                0,
                                1,
                                null
                        )
                ),
                GetSessionResponse.class
        );
        String responseGetSessionInvalidReplicaSetSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetSessionResponse(
                        new GetSessionReturn(
                                DSCResultCode.REPLICA_SET_NOT_FOUND.getResultCode(),
                                0,
                                1,
                                null
                        )
                ),
                GetSessionResponse.class
        );
        String responseGetSessionOkSoapMessage = XmlTestUtils.GetSoapMessage(
                new GetSessionResponse(
                        new GetSessionReturn(
                                DSCResultCode.OK.getResultCode(),
                                0,
                                1,
                                getSessionDataReturns
                        )
                ),
                GetSessionResponse.class
        );
        String responseTerminateSessionOkSoapMessage = XmlTestUtils.GetSoapMessage(
                new TerminateSessionResponse(
                        new TerminateSessionReturn(
                                DSCResultCode.OK.getResultCode(),
                                0,
                                1,
                                false
                        )
                ),
                TerminateSessionResponse.class
        );


        // Do the tests
        given()
                .when()
                .headers(getSession)
                .body(requestGetSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(401);
        given()
                .when()
                .headers(getSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body("")
                .post("/DSess")
                .then()
                .statusCode(400);
        // Attempt to get a session without session ID
        given()
                .when()
                .headers(getSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestGetSessionNullSessionIdSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseGetSessionInvalidIdSoapMessage));
        // Now create the session
        given()
                .when()
                .headers(joinReplicaSet)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new JoinReplicaSetRequest(
                                GET_SESSION_REPLICA,
                                GET_SESSION_INSTANCE,
                                15,
                                GET_SESSION_REPLICA_SET
                        ),
                        JoinReplicaSetRequest.class
                        )
                )
                .post("/DSess")
                .then()
                .statusCode(200);
        // Now create the session which we'll be modifying
        given()
                .when()
                .headers(createSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestCreateSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseCreateSessionOkSoapMessage));
        // Attempt to idle a session with an invalid replica set
        given()
                .when()
                .headers(getSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestGetSessionInvalidReplicaSetSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseGetSessionInvalidReplicaSetSoapMessage));
        // Attempt to get a session with an invalid session id
        given()
                .when()
                .headers(getSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestGetSessionInvalidIdSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseGetSessionInvalidIdSoapMessage));
        // Now get a session which should be success
        given()
                .when()
                .headers(getSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestGetSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseGetSessionOkSoapMessage));
        // Now terminate the test session to clean up
        given()
                .when()
                .headers(terminateSession)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(requestTerminateSessionSoapMessage)
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(responseTerminateSessionOkSoapMessage));
        // And shut down the replica
        given()
                .when()
                .headers(replicaShutdown)
                .auth().basic(BA_USER, BA_PASSWORD)
                .body(XmlTestUtils.GetSoapMessage(
                        new ReplicaShutdownRequest(GET_SESSION_REPLICA),
                        ReplicaShutdownRequest.class
                        )
                )
                .post("/DSess")
                .then()
                .statusCode(200)
                .body(is(XmlTestUtils.GetSoapMessage(
                        new ReplicaShutdownResponse(
                                DSCResultCode.OK.getResultCode()
                        ),
                        ReplicaShutdownResponse.class
                        )
                        )
                );
    }
}