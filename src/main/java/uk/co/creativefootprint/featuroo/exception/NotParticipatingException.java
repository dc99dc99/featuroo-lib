package uk.co.creativefootprint.featuroo.exception;

public class NotParticipatingException extends RuntimeException {

    public NotParticipatingException() {
        super("This client is not participating in this experiment");
    }
}
