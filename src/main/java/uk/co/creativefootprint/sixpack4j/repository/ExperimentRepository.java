package uk.co.creativefootprint.sixpack4j.repository;

import org.hibernate.criterion.Restrictions;
import uk.co.creativefootprint.sixpack4j.model.Experiment;
import java.util.UUID;

public class ExperimentRepository extends BaseRepository {

    public ExperimentRepository(String driver, String connectionString, String user, String password) {
        super(driver, connectionString, user, password);
    }

    public boolean create(Experiment experiment){

        UUID id = runQuery(s -> (UUID)s.save(experiment));
        return id != null;
    }

    public Experiment get(String name){

        return (Experiment)runQuery(
                                s-> s.createCriteria( Experiment.class )
                                        .add(Restrictions.eq("name",name))
                                        .uniqueResult()
                        );
    }
}
