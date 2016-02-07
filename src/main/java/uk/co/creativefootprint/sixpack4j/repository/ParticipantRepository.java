package uk.co.creativefootprint.sixpack4j.repository;

import org.hibernate.criterion.Restrictions;
import uk.co.creativefootprint.sixpack4j.model.*;

public class ParticipantRepository extends BaseRepository {


    public ParticipantRepository(String driver, String connectionString, String user, String password) {
        super(driver, connectionString, user, password);
    }

    /*
        Gets existing participation in an experiment.  If the client isn't participating it will
        return null
         */
    public Alternative getParticipation(Experiment experiment, Client client){

        Participation participant = (Participation)runQuery(
                s-> s.createCriteria( Participation.class )
                        .add(Restrictions.eq("clientId", client.getClientId()))
                        .add(Restrictions.eq("experimentId", experiment.getId()))
                        .uniqueResult()
        );
        if(participant == null)
            return null;

        Alternative alternative = new Alternative(participant.getAlternativeName());
        alternative.setExperiment(experiment);
        return alternative;
    }

    /*
      persists the participation of a client in an experiment.  If the client
      was already participating it will return the pre-exiting alternative, otherwise it
      will return the Alternative passed in;
       */
    public Alternative recordParticipation(Experiment experiment, Client client,  Alternative alternative){

        Alternative existing = getParticipation(experiment, client);
        if(existing != null)
            return existing;

        Participation p = new Participation(experiment.getId(), client.getClientId(), alternative.getName());
        runQuery(s -> (int)s.save(p));
        return alternative;
    }
}
