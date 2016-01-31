package uk.co.creativefootprint.sixpack4j.model;

import uk.co.creativefootprint.sixpack4j.exception.TooFewAlternativesException;
import uk.co.creativefootprint.sixpack4j.repository.ParticipantRepository;

import java.util.*;

public class Experiment{

    private static int MIN_ALTERNATIVES = 2;
    private static double DEFAULT_TRAFFIC_FRACTION = 1;

    private String name;
    private String description;
    private Alternative winner;
    private double trafficFraction = DEFAULT_TRAFFIC_FRACTION;
    private List<Alternative> alternatives;
    private boolean isArchived;
    private ChoiceStrategy strategy = new UniformChoiceStrategy();
    private ParticipantRepository participantRepository;
    private RandomGenerator randomGenerator = new RandomGenerator();

    public Experiment(String name, List<Alternative> alternatives,
                      ParticipantRepository participantRepository){

        if(alternatives.size()<MIN_ALTERNATIVES)
            throw new TooFewAlternativesException(MIN_ALTERNATIVES, alternatives.size());

        this.name = name;
        this.alternatives = Collections.unmodifiableList(new ArrayList<>(alternatives));
        this.participantRepository = participantRepository;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Alternative getWinner() {
        return winner;
    }

    public void setWinner(Alternative winner) {
        this.winner = winner;
    }

    public double getTrafficFraction() {
        return trafficFraction;
    }

    public void setTrafficFraction(double trafficFraction) {
        this.trafficFraction = trafficFraction;
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public Alternative getControl() {
        return alternatives.get(0);
    }

    public ChoiceStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(ChoiceStrategy strategy) {
        this.strategy = strategy;
    }

    public void setRandomGenerator(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public ParticipationResult participate(Client client){

        Alternative alternative = participantRepository.getParticipant(this, client);

        if(alternative != null)
            return new ParticipationResult(true, alternative);

        ParticipationResult chosen = chooseAlternative(client);
        if(!chosen.isParticipating()){
            return chosen;
        }

        Alternative actual = participantRepository.recordParticipation(
                this,
                client,
                chosen.getAlternative());

        return new ParticipationResult(true, actual);
    }

    private ParticipationResult chooseAlternative(Client client){

        if(randomGenerator.getRandom() > getTrafficFraction()){
            return new ParticipationResult(false, getControl());
        }

        return new ParticipationResult(true, getStrategy().choose(this, client));
    }
}