package com.hubert.machinelearning.agens;

import java.util.*;

import com.hubert.machinelearning.distance.*;

public class AgensAnalyzer<T> {
    public AgensAnalyzer(Collection<T> source) {
        for (T entity : source) {
            LeafNode<T> leaf = new LeafNode<T>(entity);
            mLeafNodes.add(leaf);
        }
    }

    public CompositeNode<T> analyze(IDistanceCalculator<CompositeNode<T>> calculator) {

        mCurrentClusterNodeId = 1;
        List<CompositeNode<T>> compositeNodes = new ArrayList<CompositeNode<T>>();
        for (LeafNode<T> leaf : mLeafNodes) {
            CompositeNode<T> node = new CompositeNode<T>(mCurrentClusterNodeId);
            node.add(leaf);
            compositeNodes.add(node);
        }

        DistanceCacheProxy<CompositeNode<T>> proxy = new DistanceCacheProxy<CompositeNode<T>>(calculator);

        analyze(compositeNodes, proxy);

        return mRoot;
    }

    private void analyze(List<CompositeNode<T>> nodes, IDistanceCalculator<CompositeNode<T>> calculator) {
        if (nodes.isEmpty()) {
            // TODO: throw exception
            return;
        }
        if (nodes.size() == 1) {
            mRoot = nodes.get(0);
            return;
        }

        List<CompositeNode<T>> temp = new ArrayList<CompositeNode<T>>();

        //
        CompositeNode<T> x = null;
        CompositeNode<T> y = null;
        double minDistance = -1000;

        for (CompositeNode<T> tempX : nodes) {
            for (CompositeNode<T> tempY : nodes) {
                if (tempX == tempY) {
                    continue;
                }

                double distance = calculator.distance(tempX, tempY);
                if (minDistance < 0 || distance < minDistance) {
                    minDistance = distance;
                    x = tempX;
                    y = tempY;
                }
            }
        }

        if (x == null || y == null) {
            System.out.println("Cirital error");
        }

        mCurrentClusterNodeId += 1;
        CompositeNode<T> parent = new CompositeNode<T>(mCurrentClusterNodeId);
        parent.add(x);
        parent.add(y);
        temp.addAll(nodes);
        temp.remove(x);
        temp.remove(y);
        temp.add(parent);

        analyze(temp, calculator);
    }

    private List<LeafNode<T>> mLeafNodes = new ArrayList<LeafNode<T>>();
    private CompositeNode<T> mRoot = null;
    private int mCurrentClusterNodeId = 0;
}
