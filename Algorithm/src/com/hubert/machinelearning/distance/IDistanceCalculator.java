package com.hubert.machinelearning.distance;

public interface IDistanceCalculator<T> {
    public double distance(T x, T y);
}
