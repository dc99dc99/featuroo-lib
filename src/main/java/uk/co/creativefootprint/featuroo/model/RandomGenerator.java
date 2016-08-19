package uk.co.creativefootprint.featuroo.model;

import java.util.Random;

public class RandomGenerator {

    public double getRandom(){
        Random random = new Random();
        return random.nextDouble();
    }
}
