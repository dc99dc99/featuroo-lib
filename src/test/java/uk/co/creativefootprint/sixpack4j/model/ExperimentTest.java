package uk.co.creativefootprint.sixpack4j.model;

import org.junit.Before;
import org.junit.Test;
import uk.co.creativefootprint.sixpack4j.exception.TooFewAlternativesException;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

public class ExperimentTest{

    Experiment experiment1;

    @Before
    public void before(){
        experiment1 = new Experiment("experiment1",
                Arrays.asList(
                        new Alternative("a"),
                        new Alternative("b"),
                        new Alternative("c")
                )
        );
    }

    @Test
    public void testGetAlternatives(){

        assertThat(experiment1.getAlternatives(),
                hasItems(new Alternative("a"),
                         new Alternative("b"),
                         new Alternative("b")));
    }

    @Test
    public void testAddAlternative(){

        experiment1.addAlternative(new Alternative("d"));

        assertThat(experiment1.getAlternatives(),
                hasItems(new Alternative("a"),
                        new Alternative("b"),
                        new Alternative("c"),
                        new Alternative("c"))
        );

    }

    @Test
    public void testRemoveAlternativeNotPresent(){

        experiment1.removeAlternative(new Alternative("d"));

        assertThat(experiment1.getAlternatives(),
                hasItems(new Alternative("a"),
                        new Alternative("b"),
                        new Alternative("c"))
        );

    }

    @Test(expected=TooFewAlternativesException.class)
    public void testRemoveAlternativeTooFewAlternatives(){
        experiment1.removeAlternative(new Alternative("c"));
        experiment1.removeAlternative(new Alternative("b"));
    }

    public void testRemoveAlternativeOk(){
        experiment1.removeAlternative(new Alternative("c"));

        assertThat(experiment1.getAlternatives(),
                hasItems(new Alternative("a"),
                        new Alternative("b"),
                        new Alternative("d"))
        );
    }

    @Test
    public void testParticipate(){

    }
}