package uk.co.creativefootprint.sixpack4j.exception;

public class TooFewAlternativesException extends RuntimeException{

    private int required;
    private int supplied;

    public TooFewAlternativesException(int required, int supplied){

        this.required = required;
        this.supplied = supplied;
    }

    @Override
    public String toString() {
        return String.format("Experiments require at least %s alternatives. %s supplied",
                required,
                supplied);
    }
}
