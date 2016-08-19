package uk.co.creativefootprint.featuroo.exception;


public class ExperimentNotFoundException extends RuntimeException {

    private String name;

    public ExperimentNotFoundException(String name){

        this.name = name;
    }

    @Override
    public String toString() {
        return "Experiment \'" + name + "\' was not found";
    }
}
