package uk.co.creativefootprint.sixpack4j.model;

public class ParticipationRecord {

    private int id;
    private Experiment experiment;
    private Client client;
    private String alternativeName;

    public ParticipationRecord() {
    }

    public ParticipationRecord(Experiment experiment, Client client, Alternative alternative) {
        this.experiment = experiment;
        this.client = client;
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
}
