package uk.co.creativefootprint.featuroo.exception;

public class InvalidExperimentException extends RuntimeException {

    private String message;

    public InvalidExperimentException(String message){

        this.message = message;
    }

    @Override
    public String toString() {
        return "InvalidExperimentException{" +
                "message='" + message + '\'' +
                '}';
    }
}
