package com.hubert.training;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

public class PrescriptionAnalyzer {
	public PrescriptionAnalyzer(List<String> prescriptions) {
		for (String item : prescriptions) {
			String[] herbs = item.split("\\s+");
			HashSet<String> set = new HashSet<String>();

			for (String herb : herbs) {
				herb = StringUtils.strip(herb);
				if (herb.isEmpty()) {
					continue;
				}
				set.add(herb);
				if (!mHerbCount.containsKey(herb)) {
					mHerbCount.put(herb, 1);
				} else {
					Integer count = mHerbCount.get(herb) + 1;
					mHerbCount.put(herb, count);
				}
			}
			if (set.isEmpty()){
				System.out.println("empty prescription. Ignore");
				continue;
			}
			mPrescriptions.add(set);
		}
	}

	private List<HashSet<String>> mPrescriptions = new ArrayList<HashSet<String>>();
	private HashMap<String, Integer> mHerbCount = new HashMap<String, Integer>();
	private JaccardDistanceCalculator<String> mDistanceCalculator = new JaccardDistanceCalculator<String>();
}
