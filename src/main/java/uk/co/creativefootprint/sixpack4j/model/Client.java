package uk.co.creativefootprint.sixpack4j.model;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Client {

    private String clientId;

    private Client() {
    }

    public Client(String clientId){
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public short getHash(Experiment experiment) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String salt = String.format("%s.%s", experiment.getName(), clientId);

        MessageDigest cript = MessageDigest.getInstance("SHA-1");
        cript.reset();
        cript.update(salt.getBytes("utf8"));

        byte[] digest = Arrays.copyOf(cript.digest(),7);
        return ByteBuffer.wrap(digest).getShort();
    }
}
