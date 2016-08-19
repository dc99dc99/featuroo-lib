package uk.co.creativefootprint.featuroo.model;

import java.util.Date;

public class ConversionRecord {

    private int id;
    private Experiment experiment;
    private Client client;
    private String goal;
    Date dateTime;

    ConversionRecord() {
    }

    public ConversionRecord(Experiment experiment, Client client, String goal, Date dateTime) {
        this.experiment = experiment;
        this.client = client;
        this.goal = goal;
        this.dateTime = dateTime;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public Client getClient() {
        return client;
    }

    public String getGoal() {
        return goal;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
