package uk.co.creativefootprint.featuroo.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.creativefootprint.featuroo.exception.ExperimentNotFoundException;
import uk.co.creativefootprint.featuroo.exception.NotParticipatingException;
import uk.co.creativefootprint.featuroo.model.*;
import uk.co.creativefootprint.featuroo.repository.ConversionRepository;
import uk.co.creativefootprint.featuroo.repository.ExperimentRepository;
import uk.co.creativefootprint.featuroo.repository.ParticipantRepository;

import java.util.Date;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.AdditionalMatchers.lt;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExperimentServiceTest {

    @Mock
    Experiment mockExperiment;
    @Mock
    ParticipantRepository mockParticipantRepository;
    @Mock
    ExperimentRepository mockExperimentRepository;
    @Mock
    ConversionRepository mockConversionRepository;
    @Mock
    Client mockClient;
    @Captor
    ArgumentCaptor<Experiment> experimentArgumentCaptor;
    @Mock
    ChoiceStrategy mockChoiceStrategy;

    ExperimentService experimentService;

    @Before
    public void Setup(){

        experimentService = new ExperimentService(mockExperimentRepository,
                                                  mockParticipantRepository,
                                                  mockConversionRepository,
                                                  100);
    }

    @Test(expected = ExperimentNotFoundException.class)
    public void testMissingExperiment(){

        when(mockExperimentRepository.get(any(String.class))).thenThrow(ExperimentNotFoundException.class);

        experimentService.participate("experiment", mockClient, new Date());

        verify(mockParticipantRepository, never()).getParticipation(any(Experiment.class),any(Client.class));
        verify(mockExperiment, never()).participate(any(Client.class));
        verify(mockParticipantRepository, never()).recordParticipation(any(Experiment.class),any(Client.class), any(Alternative.class), any(Date.class));

    }

    @Test
    public void testParticipateNotParticipatingAlready(){

        Alternative chosenAlternative = new Alternative("a");

        when(mockExperimentRepository.get("experiment")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipation(mockExperiment, mockClient)).thenReturn(null);
        when(mockExperiment.participate(mockClient)).thenReturn(new ParticipationResult(mockClient, true, chosenAlternative));
        when(mockParticipantRepository.recordParticipation(eq(mockExperiment), eq(mockClient),  eq(chosenAlternative), any(Date.class))).thenReturn(chosenAlternative);

        ParticipationResult result = experimentService.participate("experiment", mockClient, new Date());

        assertThat(result, is(new ParticipationResult(mockClient, true, chosenAlternative)));
    }

    @Test
    public void testParticipateWhenIsParticipatingAlready(){

        Alternative preExistingAlternative = new Alternative("b");

        when(mockExperimentRepository.get("experiment")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipation(mockExperiment, mockClient)).thenReturn(preExistingAlternative);

        ParticipationResult result = experimentService.participate("experiment", mockClient, new Date());

        verify(mockExperiment, never()).participate(any(Client.class));
        verify(mockParticipantRepository, never()).recordParticipation(any(Experiment.class), any(Client.class), any(Alternative.class), any(Date.class));

        assertThat(result, is(new ParticipationResult(mockClient, true, preExistingAlternative)));
    }

    @Test
    public void testParticipateWhenSaveIndicatesClientIsParticipatingAlready(){

        Alternative foundAlternative = new Alternative("a");
        Alternative chosenAlternative = new Alternative("b");

        when(mockExperimentRepository.get("experiment")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipation(mockExperiment, mockClient)).thenReturn(null);
        when(mockExperiment.participate(mockClient)).thenReturn(new ParticipationResult(mockClient, true, chosenAlternative));
        when(mockParticipantRepository.recordParticipation(mockExperiment, mockClient, chosenAlternative, new Date())).thenReturn(foundAlternative);

        ParticipationResult result = experimentService.participate("experiment", mockClient, new Date());

        assertThat(result, is(new ParticipationResult(mockClient, true, foundAlternative)));
    }

    @Test
    public void testFailToParticipate(){

        Alternative experimentControl = new Alternative("a");

        when(mockExperimentRepository.get("experiment")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipation(mockExperiment, mockClient)).thenReturn(null);
        when(mockExperiment.participate(mockClient)).thenReturn(new ParticipationResult(mockClient, false, experimentControl));

        ParticipationResult result = experimentService.participate("experiment", mockClient, new Date());

        verify(mockParticipantRepository, never()).recordParticipation(any(Experiment.class),any(Client.class), any(Alternative.class), any(Date.class));
        assertThat(result, is(new ParticipationResult(mockClient, false, experimentControl)));
    }

    @Test
    public void testCreateExperiment(){

        when(mockExperimentRepository.create(experimentArgumentCaptor.capture())).thenReturn(true);

        Experiment result = experimentService.createExperiment(
                "experiment test",
                "experiment description",
                Arrays.asList("a", "b"),
                0.1,
                mockChoiceStrategy
                );

        assertThat(experimentArgumentCaptor.getValue().getName(), is("experiment test"));
        assertThat(experimentArgumentCaptor.getValue().getDescription(), is("experiment description"));
        assertThat(experimentArgumentCaptor.getValue().getAlternatives(),
                hasItems(
                new Alternative("a"),
                new Alternative("b")
                )
        );
        assertThat(experimentArgumentCaptor.getValue().getTrafficFraction(), is(0.1));
        assertThat(experimentArgumentCaptor.getValue().getStrategy(), is(mockChoiceStrategy));

        assertThat(result.getName(), is("experiment test"));
        assertThat(result.getDescription(), is("experiment description"));
        assertThat(result.getAlternatives(),
                hasItems(
                        new Alternative("a"),
                        new Alternative("b")
                )
        );
        assertThat(result.getTrafficFraction(), is(0.1));
        assertThat(result.getStrategy(), is(mockChoiceStrategy));
    }

    @Test
    public void testCreateExperimentFullProperties(){

        when(mockExperimentRepository.create(experimentArgumentCaptor.capture())).thenReturn(true);

        Experiment result = experimentService.createExperiment("experiment test",
                Arrays.asList("a", "b"));

        assertThat(experimentArgumentCaptor.getValue().getName(), is("experiment test"));
        assertThat(experimentArgumentCaptor.getValue().getAlternatives(),
                hasItems(
                        new Alternative("a"),
                        new Alternative("b")
                )
        );

        assertThat(result.getName(), is("experiment test"));
        assertThat(result.getAlternatives(),
                hasItems(
                        new Alternative("a"),
                        new Alternative("b")
                )
        );
    }

    @Test
    public void testCreateExperimentFails(){

        when(mockExperimentRepository.create(experimentArgumentCaptor.capture())).thenReturn(false);

        Experiment result = experimentService.createExperiment("experiment test", Arrays.asList("a", "b"));

        assertThat(experimentArgumentCaptor.getValue().getName(), is("experiment test"));
        assertThat(experimentArgumentCaptor.getValue().getAlternatives(),
                hasItems(
                        new Alternative("a"),
                        new Alternative("b")
                )
        );

        assertThat(result, is(nullValue()));
    }
    
    @Test
    public void getExperiment(){
        
        when(mockExperimentRepository.get("Experiment 1")).thenReturn(mockExperiment);
        
        Experiment result = experimentService.getExperiment("Experiment 1");

        assertThat(result, is(mockExperiment));
    }

    @Test
    public void getExperimentDoesntExist(){

        when(mockExperimentRepository.get("Experiment 1")).thenReturn(null);

        Experiment result = experimentService.getExperiment("Experiment 1");

        assertThat(result, is(nullValue()));
    }

    @Test
    public void testConvertNoGoal() throws Exception {

        Alternative existingAlternative = new Alternative("a");

        when(mockExperimentRepository.get("Experiment 1")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipation(mockExperiment, mockClient)).thenReturn(existingAlternative);

        Date now = new Date();
        experimentService.convert("Experiment 1", mockClient, now);

        verify(mockExperiment).convert(mockClient, null);
        verify(mockConversionRepository).convert(eq(mockExperiment), eq(mockClient), eq(null), eq(now));
    }

    @Test
    public void testConvertWithGoal() throws Exception {

        Alternative existingAlternative = new Alternative("a");

        when(mockExperimentRepository.get("Experiment 1")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipation(mockExperiment, mockClient)).thenReturn(existingAlternative);

        Goal goal = new Goal("goal1");
        Date now = new Date();
        experimentService.convert("Experiment 1", mockClient, now, goal);

        verify(mockExperiment).convert(mockClient, goal);
        verify(mockConversionRepository).convert(mockExperiment, mockClient, goal, now);
    }

    @Test(expected = NotParticipatingException.class)
    public void testConvertNotParticipating() throws Exception {

        when(mockExperimentRepository.get("Experiment 1")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipation(mockExperiment, mockClient)).thenReturn(null);

        Goal goal = new Goal("goal1");
        experimentService.convert("Experiment 1", mockClient, new Date(), goal);

        verify(mockExperiment, never()).convert(any(Client.class), any(Goal.class));
        verify(mockConversionRepository, never()).convert(any(Experiment.class), any(Client.class), any(Goal.class), any(Date.class));
    }
}