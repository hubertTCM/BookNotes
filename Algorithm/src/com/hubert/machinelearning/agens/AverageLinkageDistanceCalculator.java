package com.hubert.machinelearning.agens;

import com.hubert.machinelearning.distance.IDistanceCalculator;

public class AverageLinkageDistanceCalculator<T> implements IDistanceCalculator<CompositeNode<T>> {

    public AverageLinkageDistanceCalculator(IDistanceCalculator<T> sistanceCalculator) {
        mDistanceCalculator = sistanceCalculator;
    }

    @Override
    public double distance(CompositeNode<T> x, CompositeNode<T> y) {
        double total = 0;
        for (LeafNode<T> xLeaf : x.getLeafNodes()) {
            for (LeafNode<T> yLeaf : y.getLeafNodes()) {
                double distance = this.mDistanceCalculator.distance(xLeaf.getValue(), yLeaf.getValue());
                total += distance;
            }
        }

        return total / (x.getLeafNodes().size() + y.getLeafNodes().size());
    }

    private IDistanceCalculator<T> mDistanceCalculator;

}
