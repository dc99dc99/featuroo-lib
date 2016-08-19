package uk.co.creativefootprint.featuroo.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientTest {

    @Mock
    Experiment experiment;

    @Test
    public void testGetHash() throws Exception {

        when(experiment.getName()).thenReturn("experiment1");
        Client client = new Client("a");
        assertThat(client.getHash(experiment), not(nullValue()));
    }
}