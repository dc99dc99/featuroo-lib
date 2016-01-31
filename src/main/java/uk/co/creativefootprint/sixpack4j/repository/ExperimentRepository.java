package uk.co.creativefootprint.sixpack4j.repository;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.co.creativefootprint.sixpack4j.exception.ExperimentNotFoundException;
import uk.co.creativefootprint.sixpack4j.model.Experiment;

public class ExperimentRepository {

    public boolean create(Experiment experiment){
        throw new NotImplementedException();
    }

    public Experiment get(String name) throws ExperimentNotFoundException{
        throw new NotImplementedException();
    }
}
