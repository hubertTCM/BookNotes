package com.hubert.training;

import java.util.*;

public class DistanceCacheProxy<T> implements IDistanceCalculator<T> {

	public DistanceCacheProxy(IDistanceCalculator<T> calculator, IStringConverter<T> converter) {
		mCalculator = calculator;
		mStringConverter = converter;
	}

	public DistanceCacheProxy(IDistanceCalculator<T> calculator) {
		this(calculator, new IStringConverter<T>() {

			@Override
			public String convert(T x) {
				return String.valueOf(x.hashCode());
			}
		});
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
