package uk.co.creativefootprint.sixpack4j.model;

import java.util.Random;

public class RandomGenerator {

    public double getRandom(){
        Random random = new Random();
        return random.nextDouble();
    }
}
