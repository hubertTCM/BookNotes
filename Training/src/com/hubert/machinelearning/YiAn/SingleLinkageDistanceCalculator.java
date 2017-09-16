package com.hubert.machinelearning.YiAn;

import java.util.*;

import com.hubert.machinelearning.*;

// D(I, J) = min {d(i, j) }
public class SingleLinkageDistanceCalculator implements IDistanceCalculator<PrescriptionClusterCompositeNode> {

	public SingleLinkageDistanceCalculator(IDistanceCalculator<PrescriptionClusterLeafNode> leafDistanceCalculator) {

		mLeafNodeDistanceCalculator = leafDistanceCalculator;
		// mDistanceCalculator = new DistanceCache<PrescriptionClusterLeafNode>(
		// new IDistanceCalculator<PrescriptionClusterLeafNode>() {
		// @Override
		// public double distance(PrescriptionClusterLeafNode x,
		// PrescriptionClusterLeafNode y) {
		// JaccardDistanceCalculator<Set<String>, String> temp = new
		// JaccardDistanceCalculator<Set<String>, String>();
		// return temp.distance(x.getHerbs(), y.getHerbs());
		// }
		// }, new IStringConverter<PrescriptionClusterLeafNode>() {
		// @Override
		// public String convert(PrescriptionClusterLeafNode x) {
		// return x.getSummary();
		// }
		// });
	}

	@Override
	public double distance(PrescriptionClusterCompositeNode x, PrescriptionClusterCompositeNode y) {
		double min = -1.0;
		for (PrescriptionClusterLeafNode xLeaf : x.getLeafNodes()) {
			for (PrescriptionClusterLeafNode yLeaf : y.getLeafNodes()) {
				double distance = this.mLeafNodeDistanceCalculator.distance(xLeaf, yLeaf);
				if (min < 0 || distance < min) {
					min = distance;
				}
			}
		}
		if (min < 0) {
			// TODO:
		}
		return min;
	}

	private IDistanceCalculator<PrescriptionClusterLeafNode> mLeafNodeDistanceCalculator;
}
