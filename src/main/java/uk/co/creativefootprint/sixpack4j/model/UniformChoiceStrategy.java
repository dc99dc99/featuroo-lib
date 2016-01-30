package uk.co.creativefootprint.sixpack4j.model;

import uk.co.creativefootprint.sixpack4j.exception.InvalidExperimentException;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class UniformChoiceStrategy implements ChoiceStrategy {
    @Override
    public Alternative choose(Experiment experiment, Client client) {

        try {
            int index = client.getHash(experiment) % experiment.getAlternatives().size();
            if (index < 0)
                index += Math.abs(experiment.getAlternatives().size());
            return experiment.getAlternatives().get(index);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new InvalidExperimentException(e.getMessage());
        }
    }
}
