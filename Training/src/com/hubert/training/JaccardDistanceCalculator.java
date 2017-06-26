package com.hubert.training;

import java.util.*;

public class JaccardDistanceCalculator<T> implements IDistanceCalculator<Set<T>> {

	@Override
	public double distance(Set<T> x, Set<T> y) {
		double intersectionCount = 0.0;
		double unionCount = x.size();

		for (T item : x) {
			if (y.contains(item)) {
				intersectionCount += 1;
			}
			else{
				unionCount += 1;
			}
		}

		return 1.0 - intersectionCount / unionCount;
	}

}
