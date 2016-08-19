package uk.co.creativefootprint.featuroo.model;

import uk.co.creativefootprint.featuroo.exception.InvalidExperimentException;
import uk.co.creativefootprint.featuroo.exception.InvalidStrategyException;
import uk.co.creativefootprint.featuroo.exception.TooFewAlternativesException;

import java.io.Serializable;
import java.util.*;

public class Experiment implements Serializable{

    private static int MIN_ALTERNATIVES = 2;
    private static double DEFAULT_TRAFFIC_FRACTION = 1;

    private UUID id;
    private String name;
    private String description;
    private Alternative winner;
    private double trafficFraction = DEFAULT_TRAFFIC_FRACTION;
    private List<Alternative> alternatives;
    private boolean isArchived;
    private ChoiceStrategy strategy;
    private Class<? extends ChoiceStrategy> strategyClass = UniformChoiceStrategy.class;
    private RandomGenerator randomGenerator = new RandomGenerator();

    Experiment(){
    }

    public Experiment(String name, List<Alternative> alternatives){

        if(alternatives.size()<MIN_ALTERNATIVES)
            throw new TooFewAlternativesException(MIN_ALTERNATIVES, alternatives.size());

        this.id = UUID.randomUUID();
        this.name = name;
        setAlternatives(alternatives);
    }

    private void setAlternatives(List<Alternative> alternatives){
        Collections.unmodifiableList(new ArrayList<>(alternatives));
        this.alternatives=alternatives;
        for(Alternative alternative:this.alternatives){
            alternative.setExperiment(this);
        }
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
        if(strategy ==null) {
            try {
                strategy = strategyClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new InvalidStrategyException(strategyClass.getCanonicalName());
            }
        }
        return strategy;
    }

    public void setStrategy(ChoiceStrategy strategy) {
        this.strategyClass = strategy.getClass();
        this.strategy = strategy;
    }

    public void setRandomGenerator(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public ParticipationResult participate(Client client){

        if(isArchived)
            return new ParticipationResult(client, false, getControl());

        if(randomGenerator.getRandom() > getTrafficFraction()){
            return new ParticipationResult(client, false, getControl());
        }

        return new ParticipationResult(client, true, getStrategy().choose(this, client));
    }

    public void convert(Client client, Goal goal){

        if(isArchived)
            throw new InvalidExperimentException("This experiment has been archived");
    }
}