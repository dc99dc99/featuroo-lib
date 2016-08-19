package uk.co.creativefootprint.featuroo.model;

public interface ChoiceStrategy {

    Alternative choose(Experiment experiment, Client client);
}
