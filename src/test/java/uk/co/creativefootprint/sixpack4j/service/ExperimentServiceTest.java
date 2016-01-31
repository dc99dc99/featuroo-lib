package uk.co.creativefootprint.sixpack4j.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.creativefootprint.sixpack4j.model.*;
import uk.co.creativefootprint.sixpack4j.repository.ExperimentRepository;
import uk.co.creativefootprint.sixpack4j.repository.ParticipantRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExperimentServiceTest {

    @Mock
    Experiment experiment;
    @Mock
    ParticipantRepository mockParticipantRepository;
    @Mock
    ExperimentRepository mockExperimentRepository;
    @Mock
    Client mockClient;

    ExperimentService experimentService;

    @Before
    public void Setup(){

        experimentService = new ExperimentService(mockExperimentRepository, mockParticipantRepository);
    }

    @Test
    public void testParticipateNotParticipatingAlready(){

        Alternative chosenAlternative = new Alternative("a");

        when(mockExperimentRepository.get("experiment")).thenReturn(experiment);
        when(mockParticipantRepository.getParticipant(experiment, mockClient)).thenReturn(null);
        when(experiment.participate(mockClient)).thenReturn(new ParticipationResult(true, chosenAlternative));
        when(mockParticipantRepository.recordParticipation(experiment, mockClient, chosenAlternative)).thenReturn(chosenAlternative);

        ParticipationResult result = experimentService.participate("experiment", mockClient);

        assertThat(result, is(new ParticipationResult(true, chosenAlternative)));
    }

    @Test
    public void testParticipateWhenIsParticipatingAlready(){

        Alternative preExistingAlternative = new Alternative("b");

        when(mockExperimentRepository.get("experiment")).thenReturn(experiment);
        when(mockParticipantRepository.getParticipant(experiment, mockClient)).thenReturn(preExistingAlternative);

        ParticipationResult result = experimentService.participate("experiment", mockClient);

        verify(experiment, never()).participate(any(Client.class));
        verify(mockParticipantRepository, never()).recordParticipation(any(Experiment.class), any(Client.class), any(Alternative.class));

        assertThat(result, is(new ParticipationResult(true, preExistingAlternative)));
    }

    @Test
    public void testParticipateWhenSaveIndicatesClientIsParticipatingAlready(){

        Alternative foundAlternative = new Alternative("a");
        Alternative chosenAlternative = new Alternative("b");

        when(mockExperimentRepository.get("experiment")).thenReturn(experiment);
        when(mockParticipantRepository.getParticipant(experiment, mockClient)).thenReturn(null);
        when(experiment.participate(mockClient)).thenReturn(new ParticipationResult(true, chosenAlternative));
        when(mockParticipantRepository.recordParticipation(experiment, mockClient, chosenAlternative)).thenReturn(foundAlternative);

        ParticipationResult result = experimentService.participate("experiment", mockClient);

        assertThat(result, is(new ParticipationResult(true, foundAlternative)));
    }

    @Test
    public void testFailToParticipate(){

        Alternative experimentControl = new Alternative("a");

        when(mockExperimentRepository.get("experiment")).thenReturn(experiment);
        when(mockParticipantRepository.getParticipant(experiment, mockClient)).thenReturn(null);
        when(experiment.participate(mockClient)).thenReturn(new ParticipationResult(false, experimentControl));

        ParticipationResult result = experimentService.participate("experiment", mockClient);

        verify(mockParticipantRepository, never()).recordParticipation(any(Experiment.class),any(Client.class), any(Alternative.class));
        assertThat(result, is(new ParticipationResult(false, experimentControl)));
    }
}