package uk.co.creativefootprint.sixpack4j.repository;

import org.hibernate.criterion.Restrictions;
import uk.co.creativefootprint.sixpack4j.model.*;

import java.util.Date;

public class ParticipantRepository extends BaseRepository {


    public ParticipantRepository(String driver, String connectionString, String user, String password) {
        super(driver, connectionString, user, password);
    }

    /*
        Gets existing participation in an experiment.  If the client isn't participating it will
        return null
         */
    public Alternative getParticipation(Experiment experiment, Client client){

        ParticipationRecord participant = (ParticipationRecord)runQuery(
                s-> s.createCriteria( ParticipationRecord.class)
                        .add(Restrictions.eq("client", client))
                        .add(Restrictions.eq("experiment", experiment))
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
    public Alternative recordParticipation(Experiment experiment, Client client, Alternative alternative, Date dateTime){

        Alternative existing = getParticipation(experiment, client);
        if(existing != null)
            return existing;

        ParticipationRecord p = new ParticipationRecord(experiment, client, alternative, dateTime);
        runQuery(s -> {s.saveOrUpdate(client);return null;});
        runQuery(s -> s.save(p));
        return alternative;
    }
}
