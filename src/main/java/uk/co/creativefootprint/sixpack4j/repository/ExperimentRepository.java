package uk.co.creativefootprint.sixpack4j.repository;

import org.hibernate.criterion.Restrictions;
import uk.co.creativefootprint.sixpack4j.model.Alternative;
import uk.co.creativefootprint.sixpack4j.model.Experiment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ExperimentRepository extends BaseRepository {

    public ExperimentRepository(String driver, String connectionString, String user, String password) {
        super(driver, connectionString, user, password);
    }

    public boolean create(Experiment experiment){

        Connection connection = getDBConnection();

        try {
            PreparedStatement experimentStatement = connection.prepareStatement(
                    "insert into Experiment(id, name,description,traffic_fraction,is_archived, strategy) values (?,?,?,?,?,?)");
            experimentStatement.setString(1, experiment.getId().toString());
            experimentStatement.setString(2, experiment.getName());
            experimentStatement.setString(3, experiment.getDescription());
            experimentStatement.setDouble(4, experiment.getTrafficFraction());
            experimentStatement.setBoolean(5, experiment.isArchived());
            experimentStatement.setString(6, experiment.getStrategy().getClass().getCanonicalName());
            experimentStatement.executeUpdate();

            for(Alternative alternative : experiment.getAlternatives()){
                PreparedStatement alternativeStatement = connection.prepareStatement(
                        "insert into Alternative(experimentId, name) values (?,?)");
                alternativeStatement.setString(1, experiment.getId().toString());
                alternativeStatement.setString(2, alternative.getName());
                alternativeStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
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
