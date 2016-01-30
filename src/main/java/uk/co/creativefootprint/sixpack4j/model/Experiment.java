package uk.co.creativefootprint.sixpack4j.model;

import uk.co.creativefootprint.sixpack4j.exception.TooFewAlternativesException;

import java.util.*;

public class Experiment{

    private static int MIN_ALTERNATIVES = 2;
    private String name;
    private String description;
    private Alternative winner;
    private float trafficFraction;
    private List<Alternative> alternatives;
    private boolean isArchived;
    private HashMap<Client, Alternative> participants;
    private ChoiceStrategy strategy = new UniformChoiceStrategy();

    public Experiment(String name, List<Alternative> alternatives){

        if(alternatives.size()<MIN_ALTERNATIVES)
            throw new TooFewAlternativesException(MIN_ALTERNATIVES, alternatives.size());

        this.participants = new HashMap<>();
        this.name = name;
        this.alternatives = new ArrayList<>(alternatives);
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

    public float getTrafficFraction() {
        return trafficFraction;
    }

    public void setTrafficFraction(float trafficFraction) {
        this.trafficFraction = trafficFraction;
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }

    public void addAlternative(Alternative alternative) {
        alternatives.add(alternative);
    }

    public void removeAlternative(Alternative alternative) {

        if(alternatives.contains(alternative)
                && alternatives.size() == MIN_ALTERNATIVES){
            throw new TooFewAlternativesException(MIN_ALTERNATIVES,
                                                  alternatives.size()-1);
        }
        alternatives.remove(alternative);
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public Alternative participate(Client client){

        //If this client has participated before, use that choice
        if(participants.containsKey(client)){
            return participants.get(client);
        }

        return chooseAlternative(client);
    }

    private Alternative chooseAlternative(Client client){

        Random random = new Random();
        if(random.nextDouble() > trafficFraction){
            return getControl();
        }

        return strategy.choose(this, client);
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
}