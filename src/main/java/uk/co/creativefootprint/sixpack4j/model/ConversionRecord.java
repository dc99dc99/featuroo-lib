package uk.co.creativefootprint.sixpack4j.model;

public class ConversionRecord {

    private int id;
    private Experiment experiment;
    private Client client;
    private String kpi;

    ConversionRecord() {
    }

    public ConversionRecord(Experiment experiment, Client client, String kpi) {
        this.experiment = experiment;
        this.client = client;
        this.kpi = kpi;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public Client getClient() {
        return client;
    }

    public String getKpi() {
        return kpi;
    }
}
