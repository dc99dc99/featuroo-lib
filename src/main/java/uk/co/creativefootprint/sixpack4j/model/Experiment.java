package uk.co.creativefootprint.sixpack4j.model;

import uk.co.creativefootprint.sixpack4j.exception.InvalidExperimentException;
import uk.co.creativefootprint.sixpack4j.exception.TooFewAlternativesException;

import java.util.*;

public class Experiment{

    private static int MIN_ALTERNATIVES = 2;
    private static double DEFAULT_TRAFFIC_FRACTION = 1;

    private UUID id;
    private String name;
    private String description;
    private Alternative winner;
    private double trafficFraction = DEFAULT_TRAFFIC_FRACTION;
    private List<Alternative> alternatives;
    private boolean isArchived;
    private ChoiceStrategy strategy = new UniformChoiceStrategy();
    private RandomGenerator randomGenerator = new RandomGenerator();

    private Experiment(){
    }

    public Experiment(String name, List<Alternative> alternatives){

        if(alternatives.size()<MIN_ALTERNATIVES)
            throw new TooFewAlternativesException(MIN_ALTERNATIVES, alternatives.size());

        this.id = UUID.randomUUID();
        this.name = name;
        this.alternatives = Collections.unmodifiableList(new ArrayList<>(alternatives));
    }

    public UUID getId() {
        return id;
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

    public void archive() {
        isArchived = true;
    }

    public void unarchive(){
        isArchived = false;
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

        if(isArchived)
            return new ParticipationResult(false, getControl());

        if(randomGenerator.getRandom() > getTrafficFraction()){
            return new ParticipationResult(false, getControl());
        }

        return new ParticipationResult(true, getStrategy().choose(this, client));
    }

    public void convert(Client client, Kpi kpi){

        if(isArchived)
            throw new InvalidExperimentException("This experiment has been archived");
    }
}