package com.hubert.machinelearning.YiAn;

import java.util.*;

import com.hubert.machinelearning.JaccardDistanceCalculator;

public class CenterCalculator {
	public CenterCalculator(List<Set<String>> sets) {
		mAllSets.addAll(sets);
		for (Set<String> item : sets) {
			for (String temp : item) {
				if (!mAllElements.containsKey(temp)) {
					mAllElements.put(temp, 1);
				} else {
					Integer count = mAllElements.get(temp) + 1;
					mAllElements.put(temp, count);
				}
			}
		}
		HerbCountComparator herbCountComparator = new HerbCountComparator(mAllElements);
		mLeftItems = new PriorityQueue<String>(mAllElements.size(), herbCountComparator);
	}

	public Set<String> getCenter() {
		if (mCenter == null) {
			mCenter = calculateCenter();
		}
		return mCenter;
	}

	private Set<String> calculateCenter() {
		mCenter = new HashSet<String>();
		mCenter.addAll(mAllElements.keySet());
		mLeftItems.addAll(mAllElements.keySet());

		double minDistance = distance(mCenter);
		while (!mLeftItems.isEmpty()) {
			String herb = mLeftItems.remove();
			HashSet<String> candidate = new HashSet<String>(mCenter);
			candidate.remove(herb);

			double temp = distance(candidate);
			if (temp < minDistance) {
				mCenter.clear();
				mCenter.addAll(candidate);
				minDistance = temp;
			}
		}

		return mCenter;

	}

	private double distance(Set<String> candidate) {
		double result = 0;
		// for (HashSet<String> value : mPrescriptions.values()) {
		for (Set<String> item : mAllSets) {
			result += mDistanceCalculator.distance(candidate, item);
		}
		return result;
	}

	private List<Set<String>> mAllSets = new ArrayList<Set<String>>();
	private Map<String, Integer> mAllElements = new HashMap<String, Integer>();
	private Set<String> mCenter = null;
	private PriorityQueue<String> mLeftItems = null;
	private JaccardDistanceCalculator<Set<String>, String> mDistanceCalculator = new JaccardDistanceCalculator<Set<String>, String>();

	private class HerbCountComparator implements Comparator<String> {
		public HerbCountComparator(Map<String, Integer> herbCount) {
			mHerbCount = herbCount;
		}

		@Override
		public int compare(String x, String y) {
			if (!mHerbCount.containsKey(x) || !mHerbCount.containsKey(y)) {
				System.out.println("Error, unknow herb");
				return 0;
			}

			if (mHerbCount.get(x) < mHerbCount.get(y)) {
				return -1;
			}
			if (mHerbCount.get(x) > mHerbCount.get(y)) {
				return 1;
			}

			return 0;
		}

		Map<String, Integer> mHerbCount;
	}
}
