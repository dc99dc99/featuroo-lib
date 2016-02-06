package uk.co.creativefootprint.sixpack4j.repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import uk.co.creativefootprint.sixpack4j.model.Experiment;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

public class BaseRepository {

    private String driver;
    private String connectionString;
    private String user;
    private String password;

    protected SessionFactory factory;

    public BaseRepository(String driver, String connectionString, String user, String password){

        this.driver = driver;
        this.connectionString = connectionString;
        this.user = user;
        this.password = password;

        initializeHibernate();
    }

    private void initializeHibernate(){
        try{
            Configuration configuration = new Configuration().configure();

            configuration.setProperty("hibernate.connection.driver_class", driver);
            configuration.setProperty("hibernate.connection.url", connectionString);
            configuration.setProperty("hibernate.connection.username", user);
            configuration.setProperty("hibernate.connection.password", password);
            configuration.setProperty("show_sql", "true");

            StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
            serviceRegistryBuilder.applySettings(configuration.getProperties());
            ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
            factory = configuration.buildSessionFactory(serviceRegistry);

        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void createDb(String resourceFile) throws SQLException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(resourceFile);
        java.util.Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
        getDBConnection().prepareStatement(scanner.next()).executeUpdate();
    }

    protected <T> T runQuery(Function<Session, T> method){

        Session session = factory.openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            T result = method.apply(session);
            tx.commit();
            return result;
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
            return null;
        }finally {
            session.close();
        }
    }

    protected Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(connectionString, user,
                    password);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }
}
