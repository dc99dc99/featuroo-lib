package uk.co.creativefootprint.sixpack4j.service;


import uk.co.creativefootprint.sixpack4j.model.Alternative;
import uk.co.creativefootprint.sixpack4j.model.Client;
import uk.co.creativefootprint.sixpack4j.model.Experiment;
import uk.co.creativefootprint.sixpack4j.model.ParticipationResult;
import uk.co.creativefootprint.sixpack4j.repository.ExperimentRepository;
import uk.co.creativefootprint.sixpack4j.repository.ParticipantRepository;

public class ExperimentService {

    private ExperimentRepository experimentRepository;
    private ParticipantRepository participantRepository;

    public ExperimentService(ExperimentRepository experimentRepository, ParticipantRepository participantRepository){

        this.experimentRepository = experimentRepository;
        this.participantRepository = participantRepository;
    }

    public ParticipationResult participate(String experimentName, Client client){

        Experiment experiment = experimentRepository.get(experimentName);
        Alternative alternative = participantRepository.getParticipant(experiment, client);

        if(alternative != null)
            return new ParticipationResult(true, alternative);

        ParticipationResult result = experiment.participate(client);

        if(!result.isParticipating())
            return result;

        Alternative actual = participantRepository.recordParticipation(
                experiment,
                client,
                result.getAlternative());

        return new ParticipationResult(true, actual);
    }
}
