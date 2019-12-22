package be.cronos.model.ispn;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Session {
    private String sessionId;
    private String replicaSet;
    private int sessionLimit;
    private List<SessionData> sessionData;

    @ProtoFactory
    public Session(String sessionId, String replicaSet, int sessionLimit, ArrayList<SessionData> sessionData) {
        this.sessionId = Objects.requireNonNull(sessionId);
        this.replicaSet = Objects.requireNonNull(replicaSet);
        this.sessionLimit = sessionLimit;
        this.sessionData = Objects.requireNonNull(sessionData);
    }

    @ProtoField(number = 1)
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @ProtoField(number = 2)
    public String getReplicaSet() {
        return replicaSet;
    }

    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }

    @ProtoField(number = 3, collectionImplementation = ArrayList.class)
    public List<SessionData> getSessionData() {
        return sessionData;
    }

    public void setSessionData(ArrayList<SessionData> sessionData) {
        this.sessionData = sessionData;
    }

    @ProtoField(number = 4, defaultValue = "0")
    public int getSessionLimit() {
        return sessionLimit;
    }

    public void setSessionLimit(int sessionLimit) {
        this.sessionLimit = sessionLimit;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", replicaSet='" + replicaSet + '\'' +
                ", sessionLimit=" + sessionLimit +
                ", sessionData=" + sessionData +
                '}';
    }
}
