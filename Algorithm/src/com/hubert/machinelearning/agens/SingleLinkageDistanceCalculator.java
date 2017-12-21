package com.hubert.machinelearning.agens;

//import java.util.*;

import com.hubert.machinelearning.*;
import com.hubert.machinelearning.distance.IDistanceCalculator;

// D(I, J) = min {d(i, j) }
public class SingleLinkageDistanceCalculator<T> implements IDistanceCalculator<CompositeNode<T>> {

    public SingleLinkageDistanceCalculator(IDistanceCalculator<T> distanceCalculator) {
        mDistanceCalculator = distanceCalculator;
    }

    @Override
    public double distance(CompositeNode<T> x, CompositeNode<T> y) {
        double min = -1.0;
        for (LeafNode<T> xLeaf : x.getLeafNodes()) {
            for (LeafNode<T> yLeaf : y.getLeafNodes()) {
                double distance = mDistanceCalculator.distance(xLeaf.getValue(), yLeaf.getValue());
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

    private IDistanceCalculator<T> mDistanceCalculator;
}
