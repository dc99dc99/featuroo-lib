package uk.co.creativefootprint.featuroo.repository;

import org.hibernate.criterion.Restrictions;
import uk.co.creativefootprint.featuroo.model.Client;
import uk.co.creativefootprint.featuroo.model.ConversionRecord;
import uk.co.creativefootprint.featuroo.model.Experiment;
import uk.co.creativefootprint.featuroo.model.Goal;

import java.util.Date;

public class ConversionRepository extends BaseRepository {

    public ConversionRepository(String driver, String connectionString, String user, String password) {
        super(driver, connectionString, user, password);
    }

    private ConversionRecord get(Experiment experiment, Client client, Goal goal){
        return (ConversionRecord) runQuery(
                s-> s.createCriteria( ConversionRecord.class )
                        .add(Restrictions.eq("experiment", experiment))
                        .add(Restrictions.eq("client", client))
                        .add(Restrictions.eq("goal", goal.getName()))
                        .uniqueResult()
        );
    }

    public void convert(Experiment experiment, Client client, Goal goal, Date dateTime){

        ConversionRecord existing = get(experiment, client, goal);
        if(existing!=null)
            return;

        ConversionRecord c = new ConversionRecord(experiment, client, goal.getName(), dateTime);
        runQuery(s -> {s.saveOrUpdate(client);return null;});
        runQuery(s -> s.save(c));
    }
}
