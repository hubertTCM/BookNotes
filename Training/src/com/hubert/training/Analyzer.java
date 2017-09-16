package com.hubert.training;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

public class Analyzer {
	public Analyzer(List<String> prescriptions) {
		HashMap<String, Integer> allHerbs = new HashMap<String, Integer>();
		for (String item : prescriptions) {
			String[] herbs = item.split("\\s+");
			HashSet<String> set = new HashSet<String>();

			for (String herb : herbs) {
				herb = StringUtils.strip(herb);
				if (herb.isEmpty()) {
					continue;
				}
				set.add(herb);
				if (!allHerbs.containsKey(herb)) {
					allHerbs.put(herb, 1);
				} else {
					Integer count = allHerbs.get(herb) + 1;
					allHerbs.put(herb, count);
				}
			}
			if (set.isEmpty()) {
				System.out.println("empty prescription. Ignore");
				continue;
			}
			mPrescriptions.put(item, set);
		}

		HerbCountComparator herbCountComparator = new HerbCountComparator(allHerbs);
		mLeftHerbs = new PriorityQueue<String>(allHerbs.size(), herbCountComparator);

		 calculateBestOption(allHerbs.keySet());
	}
	
	public HashSet<String> getBestOption(){
		return mBestOption;
	}

	private HashSet<String> calculateBestOption(Collection<String> allHerbs) {
		mBestOption.addAll(allHerbs);
		mLeftHerbs.addAll(allHerbs);

		double bestDistance = distance(mBestOption);
		while (!mLeftHerbs.isEmpty()) {
			String herb = mLeftHerbs.remove();
			HashSet<String> candidate = new HashSet<String>(mBestOption);
			candidate.remove(herb);

			double temp = distance(candidate);
			//System.out.println("bestDistance: " + Double.toString(bestDistance) + " temp: " + Double.toString(temp));
			if (temp < bestDistance) {
				mBestOption.clear();
				mBestOption.addAll(candidate);
				bestDistance = temp;
			}
		}

		return mBestOption;

	}

	private double distance(HashSet<String> candidate) {
		double result = 0;
		for (HashSet<String> value : mPrescriptions.values()) {
			result += mDistanceCalculator.distance(candidate, value);
		}
		return result;
	}

	private HashMap<String, HashSet<String>> mPrescriptions = new HashMap<String, HashSet<String>>();

	private HashSet<String> mBestOption = new HashSet<String>();
	private PriorityQueue<String> mLeftHerbs = null;
	private JaccardDistanceCalculator<Set<String>, String> mDistanceCalculator = new JaccardDistanceCalculator<Set<String>, String>();

	private class HerbCountComparator implements Comparator<String> {
		public HerbCountComparator(HashMap<String, Integer> herbCount) {
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

		HashMap<String, Integer> mHerbCount;
	}
}
