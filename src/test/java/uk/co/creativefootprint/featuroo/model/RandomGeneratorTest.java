package uk.co.creativefootprint.featuroo.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class RandomGeneratorTest {

    @Test
    public void testGetRandom() throws Exception {

        RandomGenerator r = new RandomGenerator();
        assertThat(r.getRandom(), instanceOf(double.class));
    }
}