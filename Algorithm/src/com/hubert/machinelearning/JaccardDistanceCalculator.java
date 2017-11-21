package com.hubert.machinelearning;

import java.util.*;

public class JaccardDistanceCalculator<T extends Set<U>, U> implements IDistanceCalculator<T> {

    @Override
    public double distance(T x, T y) {
        double intersectionCount = 0.0;
        double unionCount = y.size();

        for (U item : x) {
            if (y.contains(item)) {
                intersectionCount += 1;
            } else {
                unionCount += 1;
            }
        }

        return 1.0 - intersectionCount / unionCount;
    }

}
