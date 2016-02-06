package uk.co.creativefootprint.sixpack4j.repository;

import org.hibernate.criterion.Restrictions;
import uk.co.creativefootprint.sixpack4j.model.Alternative;
import uk.co.creativefootprint.sixpack4j.model.Experiment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
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

        List<Experiment> experiments = (List<Experiment>)runQuery(
                                                s-> s.createCriteria( Experiment.class )
                                                        .add(Restrictions.eq("name",name))
                                                        .list()
                                        );
        return experiments.get(0);
    }
}
