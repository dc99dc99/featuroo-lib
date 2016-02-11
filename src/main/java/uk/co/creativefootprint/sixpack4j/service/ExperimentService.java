package uk.co.creativefootprint.sixpack4j.service;

import uk.co.creativefootprint.sixpack4j.exception.NotParticipatingException;
import uk.co.creativefootprint.sixpack4j.model.*;
import uk.co.creativefootprint.sixpack4j.repository.ConversionRepository;
import uk.co.creativefootprint.sixpack4j.repository.ExperimentRepository;
import uk.co.creativefootprint.sixpack4j.repository.ParticipantRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ExperimentService {

    private ExperimentRepository experimentRepository;
    private ParticipantRepository participantRepository;
    private ConversionRepository conversionRepository;

    public ExperimentService(ExperimentRepository experimentRepository,
                             ParticipantRepository participantRepository,
                             ConversionRepository conversionRepository){

        this.experimentRepository = experimentRepository;
        this.participantRepository = participantRepository;
        this.conversionRepository = conversionRepository;
    }

    public Experiment getExperiment(String name){
        return experimentRepository.get(name);
    }

    public Experiment createExperiment(String name, String description, List<String> alternatives, double trafficFraction,
                                       ChoiceStrategy choiceStrategy){

        List<Alternative> alternativeItems = alternatives.stream().map(s -> new Alternative(s)).collect(Collectors.toList());
        Experiment experiment = new Experiment(name, alternativeItems);
        experiment.setDescription(description);
        experiment.setTrafficFraction(trafficFraction);
        experiment.setStrategy(choiceStrategy);

        boolean success = experimentRepository.create(experiment);
        return success ? experiment : null;
    }

    public Experiment createExperiment(String name, List<String> alternatives){
        List<Alternative> alternativeItems = alternatives.stream().map(s -> new Alternative(s)).collect(Collectors.toList());
        Experiment experiment = new Experiment(name, alternativeItems);

        boolean success = experimentRepository.create(experiment);
        return success ? experiment : null;
    }

    public ParticipationResult participate(String experimentName, Client client, Date dateTime){

        Experiment experiment = experimentRepository.get(experimentName);
        Alternative alternative = participantRepository.getParticipation(experiment, client);

        if(alternative != null)
            return new ParticipationResult(client, true, alternative);

        ParticipationResult result = experiment.participate(client);

        if(!result.isParticipating())
            return result;

        Alternative actual = participantRepository.recordParticipation(
                experiment,
                client,
                result.getAlternative(),
                dateTime
        );

        return new ParticipationResult(client, true, actual);
    }

    public Alternative convert(String name, Client client, Date dateTime){
        return convert(name, client, dateTime, null);
    }

    public Alternative convert(String name, Client client, Date dateTime, String kpi) throws NotParticipatingException{

        Experiment experiment = experimentRepository.get(name);

        Alternative alternative = participantRepository.getParticipation(experiment, client);
        if(alternative == null){
            throw new NotParticipatingException();
        }

        experiment.convert(client, kpi);

        conversionRepository.convert(experiment, client, kpi, dateTime);

        return alternative;
    }
}
