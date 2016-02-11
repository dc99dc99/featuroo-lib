package uk.co.creativefootprint.sixpack4j.model;

import java.util.Date;

public class ParticipationRecord {

    private int id;
    private Experiment experiment;
    private Client client;
    private String alternativeName;
    Date dateTime;

    ParticipationRecord() {
    }

    public ParticipationRecord(Experiment experiment, Client client, Alternative alternative, Date dateTime) {
        this.experiment = experiment;
        this.client = client;
        this.dateTime = dateTime;
        this.alternativeName = alternative.getName();
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public Client getClient() {
        return client;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
