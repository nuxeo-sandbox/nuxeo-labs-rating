/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     
 */
package org.nuxeo.labs.rating.core.test;

import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.labs.rating.utils.Average;

/**
 * Created by Michaël on 30/05/2015.
 */
public class    TestAverageComputation {

    @Test
    public void testAddToEmptyAvg() {
        Assert.assertEquals(1, Average.addValueToAverage(1, 0, 0), 0.1f);
    }

    @Test
    public void testAddSameToAvg() {
        Assert.assertEquals(5, Average.addValueToAverage(5, 5, 1), 0.1f);
    }

    @Test
    public void testAddToAvg() {
        Assert.assertEquals(3, Average.addValueToAverage(1, 5, 1), 0.1f);
    }

    @Test
    public void testRemoveFromEmptyAvg() {
        Assert.assertEquals(0, Average.removeValueFromAverage(0, 0, 0), 0.1f);
    }

    @Test
    public void testRemoveFromAvg() {
        Assert.assertEquals(0, Average.removeValueFromAverage(3, 3, 1), 0.1f);
    }

    @Test
    public void testRemoveFromAvg2() {
        Assert.assertEquals(2, Average.removeValueFromAverage(4, 3, 2), 0.1f);
    }

}