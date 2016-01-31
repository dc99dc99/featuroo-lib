package uk.co.creativefootprint.sixpack4j.exception;

import uk.co.creativefootprint.sixpack4j.model.Client;

public class NotParticipatingException extends RuntimeException {

    public NotParticipatingException() {
        super("This client is not participating in this experiment");
    }
}
