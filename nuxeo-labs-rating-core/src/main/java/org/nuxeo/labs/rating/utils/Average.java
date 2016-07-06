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
package org.nuxeo.labs.rating.utils;

/**
 * Created by Michaël on 2/24/2016.
 */
public class Average {

    public static double addValueToAverage(long value, double average, long count) {
        float coeff = 1 / (count + 1.0f);
        return average * (count * coeff) + value * coeff;
    }

    public static double removeValueFromAverage(long value, double average, long count) {
        if (count <= 1)
            return 0f;
        float coeff = 1 / (count - 1.0f);
        return average * (count * coeff) - value * coeff;
    }
}
