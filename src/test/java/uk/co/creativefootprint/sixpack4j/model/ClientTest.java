package uk.co.creativefootprint.sixpack4j.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientTest {

    @Mock
    Experiment experiment;

    @Test
    public void testGetHash() throws Exception {

        when(experiment.getName()).thenReturn("experiment1");
        Client client = new Client("a");
        System.out.println(client.getHash(experiment));
    }
}