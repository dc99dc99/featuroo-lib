package uk.co.creativefootprint.sixpack4j.repository;

import uk.co.creativefootprint.sixpack4j.model.Alternative;
import uk.co.creativefootprint.sixpack4j.model.ChoiceStrategy;
import uk.co.creativefootprint.sixpack4j.model.Client;
import uk.co.creativefootprint.sixpack4j.model.Experiment;

public class TestChoiceStrategy implements ChoiceStrategy {

    @Override
    public Alternative choose(Experiment experiment, Client client) {
        return null;
    }
}
