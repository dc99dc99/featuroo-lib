package uk.co.creativefootprint.featuroo.repository;

import uk.co.creativefootprint.featuroo.model.Alternative;
import uk.co.creativefootprint.featuroo.model.ChoiceStrategy;
import uk.co.creativefootprint.featuroo.model.Client;
import uk.co.creativefootprint.featuroo.model.Experiment;

public class TestChoiceStrategy implements ChoiceStrategy {

    @Override
    public Alternative choose(Experiment experiment, Client client) {
        return null;
    }
}
