package uk.co.creativefootprint.sixpack4j.repository;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.co.creativefootprint.sixpack4j.model.Alternative;
import uk.co.creativefootprint.sixpack4j.model.Client;
import uk.co.creativefootprint.sixpack4j.model.Experiment;
import uk.co.creativefootprint.sixpack4j.model.ParticipationResult;

public class ParticipantRepository {

    /*
    Gets existing participation in an experiment.  If the client isn't participating it will
    return null
     */
    public Alternative getParticipant(Experiment experiment, Client client){
        throw new NotImplementedException();
    }

    /*
      persists the participation of a client in an experiment.  If the client
      was already participating it will return true and the pre-exiting alternative, otherwise it
      will return false and Alternative passed in;
       */
    public Alternative recordParticipation(Experiment experiment, Client client, Alternative alternative){
        throw new NotImplementedException();
    }
}
