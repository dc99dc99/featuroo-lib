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
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ParticipantRepositoryTest {

    ParticipantRepository repository;

    private static String DB_DRIVER="org.h2.Driver";
    private static String DB_CONNECTION="jdbc:h2:~/sixpack-test";
    private static String DB_USER="";
    private static String DB_PASSWORD="";

    @Before
    public void before() throws SQLException {

        repository = new ParticipantRepository(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
        deleteDb();
        repository.createDb("db.sql");
    }
    
    @Test
    public void participateNotParticipatingAlready() {
        //arrange
        Experiment experiment = new Experiment("example experiment",
                Arrays.asList(
                        new Alternative("a"),
                        new Alternative("b")
                ));

        Alternative result = repository.recordParticipation(experiment, new Client("my client"), new Alternative("a"));
        assertThat(result, is(new Alternative("a")));
    }

    @Test
    public void participateIsParticipatingAlready() {
        //arrange
        Experiment experiment = new Experiment("example experiment",
                Arrays.asList(
                        new Alternative("a"),
                        new Alternative("b")
                ));

        Alternative result = repository.recordParticipation(experiment, new Client("my client"), new Alternative("a"));
        assertThat(result, is(new Alternative("a")));
        Alternative result2 = repository.recordParticipation(experiment, new Client("my client"), new Alternative("b"));
        assertThat(result2, is(new Alternative("a")));
    }

    @Test
    public void getParticipation(){

        //arrange
        Experiment experiment = new Experiment("example experiment",
                Arrays.asList(
                        new Alternative("a"),
                        new Alternative("b")
                ));
        Client client = new Client("my client");

        Alternative result = repository.recordParticipation(experiment, client, new Alternative("b"));

        Alternative read = repository.getParticipation(experiment, client);
        assertThat(read,is(new Alternative("b")));
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