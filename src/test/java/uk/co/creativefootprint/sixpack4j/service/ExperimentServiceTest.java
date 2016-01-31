package uk.co.creativefootprint.sixpack4j.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.creativefootprint.sixpack4j.exception.ExperimentNotFoundException;
import uk.co.creativefootprint.sixpack4j.model.*;
import uk.co.creativefootprint.sixpack4j.repository.ConversionRepository;
import uk.co.creativefootprint.sixpack4j.repository.ExperimentRepository;
import uk.co.creativefootprint.sixpack4j.repository.ParticipantRepository;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
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
                                                  mockConversionRepository);
    }

    @Test(expected = ExperimentNotFoundException.class)
    public void testMissingExperiment(){

        when(mockExperimentRepository.get(any(String.class))).thenThrow(ExperimentNotFoundException.class);

        experimentService.participate("experiment", mockClient);

        verify(mockParticipantRepository, never()).getParticipant(any(Experiment.class),any(Client.class));
        verify(mockExperiment, never()).participate(any(Client.class));
        verify(mockParticipantRepository, never()).recordParticipation(any(Experiment.class),any(Client.class), any(Alternative.class));

    }

    @Test
    public void testParticipateNotParticipatingAlready(){

        Alternative chosenAlternative = new Alternative("a");

        when(mockExperimentRepository.get("experiment")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipant(mockExperiment, mockClient)).thenReturn(null);
        when(mockExperiment.participate(mockClient)).thenReturn(new ParticipationResult(true, chosenAlternative));
        when(mockParticipantRepository.recordParticipation(mockExperiment, mockClient, chosenAlternative)).thenReturn(chosenAlternative);

        ParticipationResult result = experimentService.participate("experiment", mockClient);

        assertThat(result, is(new ParticipationResult(true, chosenAlternative)));
    }

    @Test
    public void testParticipateWhenIsParticipatingAlready(){

        Alternative preExistingAlternative = new Alternative("b");

        when(mockExperimentRepository.get("experiment")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipant(mockExperiment, mockClient)).thenReturn(preExistingAlternative);

        ParticipationResult result = experimentService.participate("experiment", mockClient);

        verify(mockExperiment, never()).participate(any(Client.class));
        verify(mockParticipantRepository, never()).recordParticipation(any(Experiment.class), any(Client.class), any(Alternative.class));

        assertThat(result, is(new ParticipationResult(true, preExistingAlternative)));
    }

    @Test
    public void testParticipateWhenSaveIndicatesClientIsParticipatingAlready(){

        Alternative foundAlternative = new Alternative("a");
        Alternative chosenAlternative = new Alternative("b");

        when(mockExperimentRepository.get("experiment")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipant(mockExperiment, mockClient)).thenReturn(null);
        when(mockExperiment.participate(mockClient)).thenReturn(new ParticipationResult(true, chosenAlternative));
        when(mockParticipantRepository.recordParticipation(mockExperiment, mockClient, chosenAlternative)).thenReturn(foundAlternative);

        ParticipationResult result = experimentService.participate("experiment", mockClient);

        assertThat(result, is(new ParticipationResult(true, foundAlternative)));
    }

    @Test
    public void testFailToParticipate(){

        Alternative experimentControl = new Alternative("a");

        when(mockExperimentRepository.get("experiment")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipant(mockExperiment, mockClient)).thenReturn(null);
        when(mockExperiment.participate(mockClient)).thenReturn(new ParticipationResult(false, experimentControl));

        ParticipationResult result = experimentService.participate("experiment", mockClient);

        verify(mockParticipantRepository, never()).recordParticipation(any(Experiment.class),any(Client.class), any(Alternative.class));
        assertThat(result, is(new ParticipationResult(false, experimentControl)));
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
    public void testConvertNoKpi() throws Exception {

        Alternative existingAlternative = new Alternative("a");

        when(mockExperimentRepository.get("Experiment 1")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipant(mockExperiment, mockClient)).thenReturn(existingAlternative);

        experimentService.convert("Experiment 1", mockClient);

        verify(mockExperiment).convert(mockClient, null);
        verify(mockConversionRepository).convert(mockClient, null);
    }

    @Test
    public void testConvertWithKpi() throws Exception {

        Alternative existingAlternative = new Alternative("a");
        Kpi kpi = new Kpi("kpi1");

        when(mockExperimentRepository.get("Experiment 1")).thenReturn(mockExperiment);
        when(mockParticipantRepository.getParticipant(mockExperiment, mockClient)).thenReturn(existingAlternative);

        experimentService.convert("Experiment 1", mockClient,"kpi1");

        verify(mockExperiment).convert(mockClient, kpi);
        verify(mockConversionRepository).convert(mockClient, kpi);
    }
}