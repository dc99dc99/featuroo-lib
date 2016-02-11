package uk.co.creativefootprint.sixpack4j.model;

import java.util.Date;

public class ConversionRecord {

    private int id;
    private Experiment experiment;
    private Client client;
    private String kpi;
    Date dateTime;

    ConversionRecord() {
    }

    public ConversionRecord(Experiment experiment, Client client, String kpi, Date dateTime) {
        this.experiment = experiment;
        this.client = client;
        this.kpi = kpi;
        this.dateTime = dateTime;
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

    public Date getDateTime() {
        return dateTime;
    }
}
