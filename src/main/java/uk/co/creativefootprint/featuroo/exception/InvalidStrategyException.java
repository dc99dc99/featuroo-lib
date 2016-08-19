package uk.co.creativefootprint.featuroo.exception;


public class InvalidStrategyException extends RuntimeException {

    private String name;

    public InvalidStrategyException(String name){

        this.name = name;
    }

    @Override
    public String toString() {
        return "Experiment \'" + name + "\' was not found";
    }
}
