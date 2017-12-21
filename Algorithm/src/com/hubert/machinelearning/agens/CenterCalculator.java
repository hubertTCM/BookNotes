package com.hubert.machinelearning.agens;

import java.util.*;

import com.hubert.machinelearning.distance.JaccardDistanceCalculator;

public class CenterCalculator<T> {
    public CenterCalculator(List<Set<T>> sets) {
        mAllSets.addAll(sets);
        for (Set<T> tempSet : sets) {
            for (T tempItem : tempSet) {
                if (!mAllElements.containsKey(tempItem)) {
                    mAllElements.put(tempItem, 1);
                } else {
                    Integer count = mAllElements.get(tempItem) + 1;
                    mAllElements.put(tempItem, count);
                }
            }
        }
        FrequencyComparator herbCountComparator = new FrequencyComparator(mAllElements);
        mLeftItems = new PriorityQueue<T>(mAllElements.size(), herbCountComparator);
    }

    public Set<T> getCenter() {
        if (mCenter == null) {
            mCenter = calculateCenter();
        }
        return mCenter;
    }

    private Set<T> calculateCenter() {
        mCenter = new HashSet<T>();
        mCenter.addAll(mAllElements.keySet());
        mLeftItems.addAll(mAllElements.keySet());

        double minDistance = distance(mCenter);
        while (!mLeftItems.isEmpty()) {
            T herb = mLeftItems.remove();
            HashSet<T> candidate = new HashSet<T>(mCenter);
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

    private double distance(Set<T> candidate) {
        double result = 0;
        // for (HashSet<String> value : mPrescriptions.values()) {
        for (Set<T> item : mAllSets) {
            result += mDistanceCalculator.distance(candidate, item);
        }
        return result;
    }

    private List<Set<T>> mAllSets = new ArrayList<Set<T>>();
    private Map<T, Integer> mAllElements = new HashMap<T, Integer>();
    private Set<T> mCenter = null;
    private PriorityQueue<T> mLeftItems = null;
    private JaccardDistanceCalculator<Set<T>, T> mDistanceCalculator = new JaccardDistanceCalculator<Set<T>, T>();

    private class FrequencyComparator implements Comparator<T> {
        public FrequencyComparator(Map<T, Integer> herbCount) {
            mHerbCount = herbCount;
        }

        @Override
        public int compare(T x, T y) {
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

        Map<T, Integer> mHerbCount;
    }
}
