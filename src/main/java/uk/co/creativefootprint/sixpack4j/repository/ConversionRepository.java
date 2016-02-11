package uk.co.creativefootprint.sixpack4j.repository;

import org.hibernate.criterion.Restrictions;
import uk.co.creativefootprint.sixpack4j.model.Client;
import uk.co.creativefootprint.sixpack4j.model.ConversionRecord;
import uk.co.creativefootprint.sixpack4j.model.Experiment;

import java.util.Date;

public class ConversionRepository extends BaseRepository {

    public ConversionRepository(String driver, String connectionString, String user, String password) {
        super(driver, connectionString, user, password);
    }

    private ConversionRecord get(Experiment experiment, Client client, String kpi){
        return (ConversionRecord) runQuery(
                s-> s.createCriteria( ConversionRecord.class )
                        .add(Restrictions.eq("experiment", experiment))
                        .add(Restrictions.eq("client", client))
                        .add(Restrictions.eq("kpi", kpi))
                        .uniqueResult()
        );
    }

    public void convert(Experiment experiment, Client client, String kpi, Date dateTime){

        ConversionRecord existing = get(experiment, client, kpi);
        if(existing!=null)
            return;

        ConversionRecord c = new ConversionRecord(experiment, client, kpi, dateTime);
        runQuery(s -> {s.saveOrUpdate(client);return null;});
        runQuery(s -> s.save(c));
    }
}
