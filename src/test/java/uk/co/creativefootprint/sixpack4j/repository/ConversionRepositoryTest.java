package uk.co.creativefootprint.sixpack4j.repository;

import org.junit.Before;
import org.junit.Test;
import uk.co.creativefootprint.sixpack4j.model.Alternative;
import uk.co.creativefootprint.sixpack4j.model.Client;
import uk.co.creativefootprint.sixpack4j.model.Experiment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConversionRepositoryTest {

    ConversionRepository repository;

    private static String DB_DRIVER="org.h2.Driver";
    private static String DB_CONNECTION="jdbc:h2:~/sixpack-test";
    private static String DB_USER="";
    private static String DB_PASSWORD="";

    private Experiment existingExperiment;

    @Before
    public void before() throws SQLException {

        repository = new ConversionRepository(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
        deleteDb();
        repository.createDb("db.sql");
        setupDb();
    }

    private void setupDb(){
        //arrange
        existingExperiment = new Experiment("example experiment",
                Arrays.asList(
                        new Alternative("a"),
                        new Alternative("b")
                ));

        ExperimentRepository e = new ExperimentRepository(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
        e.create(existingExperiment);
    }
    
    @Test
    public void convert() {

        Client client = new Client("my client");
        repository.convert(existingExperiment, client, "kpi1", new Date());
    }

    @Test
    public void convertTwiceSameKpi() {

        Client client = new Client("my client");
        repository.convert(existingExperiment, client, "kpi1", new Date());
        repository.convert(existingExperiment, client, "kpi1", new Date());
    }

    @Test
    public void convertTwiceDifferentKpi() {

        Client client = new Client("my client");
        repository.convert(existingExperiment, client, "kpi1", new Date());
        repository.convert(existingExperiment, client, "kpi2", new Date());
    }


    private void deleteDb() {
        Connection dbConnection;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
                    DB_PASSWORD);
            PreparedStatement s = dbConnection.prepareStatement("DROP ALL OBJECTS");
            s.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}