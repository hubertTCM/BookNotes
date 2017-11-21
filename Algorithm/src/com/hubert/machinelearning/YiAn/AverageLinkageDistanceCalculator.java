package com.hubert.machinelearning.YiAn;

import com.hubert.machinelearning.IDistanceCalculator;

public class AverageLinkageDistanceCalculator implements IDistanceCalculator<PrescriptionClusterCompositeNode> {

    public AverageLinkageDistanceCalculator(IDistanceCalculator<PrescriptionClusterLeafNode> leafDistanceCalculator) {

        mLeafNodeDistanceCalculator = leafDistanceCalculator;
    }

    @Override
    public double distance(PrescriptionClusterCompositeNode x, PrescriptionClusterCompositeNode y) {
        double total = 0;
        for (PrescriptionClusterLeafNode xLeaf : x.getLeafNodes()) {
            for (PrescriptionClusterLeafNode yLeaf : y.getLeafNodes()) {
                double distance = this.mLeafNodeDistanceCalculator.distance(xLeaf, yLeaf);
                total += distance;
            }
        }

        return total / (x.getLeafNodes().size() + y.getLeafNodes().size());
    }

    private IDistanceCalculator<PrescriptionClusterLeafNode> mLeafNodeDistanceCalculator;

}
