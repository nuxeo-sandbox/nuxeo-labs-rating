package org.nuxeo.labs.rating.utils;

/**
 * Created by Michaël on 2/24/2016.
 */
public class Average {

    public static double addValueToAverage(long value, double average, long count) {
        float coeff = 1/(count+1.0f);
        return average*count*coeff+value*coeff;
    }

    public static double removeValueFromAverage(long value, double average, long count) {
        if (count<=1) return 0f;
        float coeff = 1/(count-1.0f);
        return average*count*coeff-value*coeff;
    }
}
