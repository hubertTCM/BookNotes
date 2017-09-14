package com.hubert.training;

import java.util.*;

public class DistanceCache<T> implements IDistanceCalculator<T> {

	public DistanceCache(IDistanceCalculator<T> calculator, IStringConverter<T> converter) {
		mCalculator = calculator;
		mStringConverter = converter;
	}

	@Override
	public double distance(T x, T y) {
		String key = getKey(x, y);
		if (mDistance.containsKey(key)) {
			return mDistance.get(key);
		}
		double distance = mCalculator.distance(x, y);
		mDistance.put(key, distance);
		return distance;
	}

	private String getKey(T x, T y) {
		return mStringConverter.convert(x) + "##" + mStringConverter.convert(y);
	}

	private IDistanceCalculator<T> mCalculator;
	private IStringConverter<T> mStringConverter;
	private Map<String, Double> mDistance = new HashMap<String, Double>();
}
