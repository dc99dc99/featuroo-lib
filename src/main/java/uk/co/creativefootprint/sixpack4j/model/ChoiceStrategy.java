package uk.co.creativefootprint.sixpack4j.model;

public interface ChoiceStrategy {

    Alternative choose(Experiment experiment, Client client);
}
