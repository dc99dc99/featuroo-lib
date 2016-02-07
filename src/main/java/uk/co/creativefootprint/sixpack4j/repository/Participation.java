package uk.co.creativefootprint.sixpack4j.repository;

import java.util.UUID;

public class Participation {

    private int id;
    private String clientId;
    private String alternativeName;
    private UUID experimentId;

    public Participation() {
    }

    public Participation(UUID experimentId, String clientId, String alternativeName) {
        this.clientId = clientId;
        this.alternativeName = alternativeName;
        this.experimentId = experimentId;
    }

    public int getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public UUID getExperimentId() {
        return experimentId;
    }
}
