package uk.co.creativefootprint.featuroo.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UniformChoiceStrategyTest {

    @Mock
    Experiment experiment;

    @Mock
    Client client;

    List<Alternative> alternatives;
    List<Alternative> alternativesLarge;
    Alternative alternativeA = new Alternative("a");
    Alternative alternativeB = new Alternative("b");
    Alternative alternativeC = new Alternative("c");
    Alternative alternativeD = new Alternative("d");

    @Before
    public void setup(){
        alternatives = Arrays.asList(alternativeA, alternativeB);
        alternativesLarge = Arrays.asList(alternativeA, alternativeB, alternativeC, alternativeD);
    }

    @Test
    public void testChooseZeroHash() throws Exception {

        when(client.getHash(experiment)).thenReturn((short)0);
        when(experiment.getAlternatives()).thenReturn(alternatives);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, is(alternativeA));
    }

    @Test
    public void testChooseOneHash() throws Exception {

        when(client.getHash(experiment)).thenReturn((short)1);
        when(experiment.getAlternatives()).thenReturn(alternatives);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, is(alternativeB));
    }

    @Test
    public void testChooseNegativeOneHash() throws Exception {

        when(client.getHash(experiment)).thenReturn((short)-1);
        when(experiment.getAlternatives()).thenReturn(alternatives);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, is(alternativeB));
    }

    @Test
    public void testChooseLargePositiveHash() throws Exception {

        when(client.getHash(experiment)).thenReturn(Short.MAX_VALUE);
        when(experiment.getAlternatives()).thenReturn(alternatives);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, anyOf(is(alternativeA), is(alternativeB)));
    }

    @Test
    public void testChooseLargeNegativeHash() throws Exception {

        when(client.getHash(experiment)).thenReturn(Short.MIN_VALUE);
        when(experiment.getAlternatives()).thenReturn(alternatives);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, anyOf(is(alternativeA), is(alternativeB)));
    }

    @Test
    public void testLargeChooseZeroHash() throws Exception {

        when(client.getHash(experiment)).thenReturn((short)0);
        when(experiment.getAlternatives()).thenReturn(alternativesLarge);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, is(alternativeA));
    }

    @Test
    public void testLargeChooseOneHash() throws Exception {

        when(client.getHash(experiment)).thenReturn((short)1);
        when(experiment.getAlternatives()).thenReturn(alternativesLarge);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, is(alternativeB));
    }

    @Test
    public void testLargeChooseTwoHash() throws Exception {

        when(client.getHash(experiment)).thenReturn((short)2);
        when(experiment.getAlternatives()).thenReturn(alternativesLarge);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, is(alternativeC));
    }

    @Test
    public void testLargeChooseThreeHash() throws Exception {

        when(client.getHash(experiment)).thenReturn((short)3);
        when(experiment.getAlternatives()).thenReturn(alternativesLarge);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, is(alternativeD));
    }

    @Test
    public void testLargeChooseFourHash() throws Exception {

        when(client.getHash(experiment)).thenReturn((short)4);
        when(experiment.getAlternatives()).thenReturn(alternativesLarge);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, is(alternativeA));
    }

    @Test
    public void testLargeChooseNegativeOneHash() throws Exception {

        when(client.getHash(experiment)).thenReturn((short)-1);
        when(experiment.getAlternatives()).thenReturn(alternativesLarge);

        UniformChoiceStrategy strategy = new UniformChoiceStrategy();
        Alternative result = strategy.choose(experiment, client);

        assertThat(result, is(alternativeD));
    }
}