package com.hubert.machinelearning;

public interface IDistanceCalculator<T> {
	public double distance(T x, T y);
}
