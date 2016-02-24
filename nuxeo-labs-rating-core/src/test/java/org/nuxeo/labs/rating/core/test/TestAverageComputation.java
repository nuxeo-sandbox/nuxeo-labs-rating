package org.nuxeo.labs.rating.core.test;

import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.labs.rating.utils.Average;

/**
 * Created by Michaël on 30/05/2015.
 */
public class TestAverageComputation {

    @Test
    public void testAddToEmptyAvg() {
        Assert.assertEquals(1, Average.addValueToAverage(1,0,0),0.1f);
    }

    @Test
    public void testAddSameToAvg() {
        Assert.assertEquals(5, Average.addValueToAverage(5,5,1),0.1f);
    }

    @Test
    public void testAddToAvg() {
        Assert.assertEquals(3, Average.addValueToAverage(1,5,1),0.1f);
    }

    @Test
    public void testRemoveFromEmptyAvg() {
        Assert.assertEquals(0, Average.removeValueFromAverage(0,0,0),0.1f);
    }

    @Test
    public void testRemoveFromAvg() {
        Assert.assertEquals(0, Average.removeValueFromAverage(3,3,1),0.1f);
    }

    @Test
    public void testRemoveFromAvg2() {
        Assert.assertEquals(2, Average.removeValueFromAverage(4,3,2),0.1f);
    }

}