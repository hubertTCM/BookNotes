package com.hubert.YiAn;

import java.util.*;

import com.hubert.training.*;

// D(I, J) = min {d(i, j) }
public class SingleLinkageDistanceCalculator implements IDistanceCalculator<PrescriptionClusterCompositeNode> {

	public SingleLinkageDistanceCalculator() {
		mDistanceCalculator = new DistanceCache<PrescriptionClusterLeafNode>(
				new IDistanceCalculator<PrescriptionClusterLeafNode>() {
					@Override
					public double distance(PrescriptionClusterLeafNode x, PrescriptionClusterLeafNode y) {
						JaccardDistanceCalculator<Set<String>, String> temp = new JaccardDistanceCalculator<Set<String>, String>();
						return temp.distance(x.getHerbs(), y.getHerbs());
					}
				}, new IStringConverter<PrescriptionClusterLeafNode>() {
					@Override
					public String convert(PrescriptionClusterLeafNode x) {
						return x.getSummary();
					}
				});
	}

	@Override
	public double distance(PrescriptionClusterCompositeNode x, PrescriptionClusterCompositeNode y) {
		double min = -1.0;
		for (PrescriptionClusterLeafNode xLeaf : x.getLeafNodes()) {
			for (PrescriptionClusterLeafNode yLeaf : y.getLeafNodes()) {
				double distance = this.mDistanceCalculator.distance(xLeaf, yLeaf);
				if (min < 0 || distance < min){
					min = distance;
				}
			}
		}
		if (min < 0) {

		}
		return min;
	}

	private DistanceCache<PrescriptionClusterLeafNode> mDistanceCalculator;
}
