package uk.co.creativefootprint.sixpack4j.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.creativefootprint.sixpack4j.repository.ParticipantRepository;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExperimentTest{

    Experiment experiment1;

    @Mock
    Client mockClient;

    @Mock
    ChoiceStrategy mockChoiceStrategy;

    @Mock
    RandomGenerator mockRandomGenerator;

    @Before
    public void before(){
        experiment1 = new Experiment("experiment1",
                Arrays.asList(
                        new Alternative("a"),
                        new Alternative("b"),
                        new Alternative("c")
                )
        );

        when(mockClient.getClientId()).thenReturn("client1");

        experiment1.setStrategy(mockChoiceStrategy);
        experiment1.setRandomGenerator(mockRandomGenerator);
    }

    @Test
    public void testGetAlternatives(){

        assertThat(experiment1.getAlternatives(),
                hasItems(new Alternative("a"),
                         new Alternative("b"),
                         new Alternative("b")));
    }

    @Test
    public void testGetName(){

        assertThat(experiment1.getName(),
                is("experiment1"));
    }

    @Test
    public void testParticipateNotParticipatingAlready(){

        Alternative chosenAlternative = new Alternative("a");

        when(mockChoiceStrategy.choose(experiment1, mockClient)).thenReturn(chosenAlternative);
        when(mockRandomGenerator.getRandom()).thenReturn(0.0);

        ParticipationResult result = experiment1.participate(mockClient);

        assertThat(result, is(new ParticipationResult(true, chosenAlternative)));
    }

    @Test
    public void testParticipantShouldGetControlAndNotParticipateAboveTrafficFraction(){

        experiment1.setTrafficFraction(0); //no-one should participate

        when(mockRandomGenerator.getRandom()).thenReturn(1.0);

        ParticipationResult result = experiment1.participate(mockClient);

        verify(mockChoiceStrategy, never()).choose(any(Experiment.class), any(Client.class));
        assertThat(result, is(new ParticipationResult(false, experiment1.getControl())));
    }

    @Test
    public void testParticipantShouldGetControlAndNotParticipateWhenJustOverTrafficFraction(){

        experiment1.setTrafficFraction(0.5); //50% will participate
        Alternative chosenAlternative = new Alternative("b");

        when(mockRandomGenerator.getRandom()).thenReturn(0.50001); //but we are just over the threshold

        ParticipationResult result = experiment1.participate(mockClient);

        verify(mockChoiceStrategy, never()).choose(any(Experiment.class), any(Client.class));
        assertThat(result, is(new ParticipationResult(false, experiment1.getControl())));
    }

    @Test
    public void testParticipantShouldParticipateWhenMatchesTrafficFraction(){

        experiment1.setTrafficFraction(0.5); //50% will participate
        Alternative chosenAlternative = new Alternative("a");

        when(mockRandomGenerator.getRandom()).thenReturn(0.5);//matches the traffic fraction
        when(mockChoiceStrategy.choose(experiment1, mockClient)).thenReturn(chosenAlternative);

        ParticipationResult result = experiment1.participate(mockClient);

        assertThat(result, is(new ParticipationResult(true, chosenAlternative)));
    }
}