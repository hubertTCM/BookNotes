package com.hubert.machinelearning.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.machinelearning.*;

public class PrescriptionAnalyzer {
    public PrescriptionAnalyzer(List<YiAnPrescriptionEntity> source) {
        for (YiAnPrescriptionEntity entity : source) {
            PrescriptionClusterLeafNode leaf = new PrescriptionClusterLeafNode(entity);
            mLeafNodes.add(leaf);
        }
    }

    public PrescriptionClusterCompositeNode analyze(IDistanceCalculator<PrescriptionClusterCompositeNode> calculator) {

        mCurrentClusterNodeId = 1;
        List<PrescriptionClusterCompositeNode> compositeNodes = new ArrayList<PrescriptionClusterCompositeNode>();
        for (PrescriptionClusterLeafNode leaf : mLeafNodes) {
            PrescriptionClusterCompositeNode node = new PrescriptionClusterCompositeNode(mCurrentClusterNodeId);
            node.add(leaf);
            compositeNodes.add(node);
        }

        DistanceCacheProxy<PrescriptionClusterCompositeNode> proxy = new DistanceCacheProxy<PrescriptionClusterCompositeNode>(
                calculator);

        analyze(compositeNodes, proxy);

        return mRoot;
    }

    private void analyze(List<PrescriptionClusterCompositeNode> nodes,
            IDistanceCalculator<PrescriptionClusterCompositeNode> calculator) {
        if (nodes.isEmpty()) {
            // TODO: throw exception
            return;
        }
        if (nodes.size() == 1) {
            mRoot = nodes.get(0);
            return;
        }

        List<PrescriptionClusterCompositeNode> temp = new ArrayList<PrescriptionClusterCompositeNode>();

        //
        PrescriptionClusterCompositeNode x = null;
        PrescriptionClusterCompositeNode y = null;
        double minDistance = -1000;

        for (PrescriptionClusterCompositeNode tempX : nodes) {
            for (PrescriptionClusterCompositeNode tempY : nodes) {
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
        PrescriptionClusterCompositeNode parent = new PrescriptionClusterCompositeNode(mCurrentClusterNodeId);
        parent.add(x);
        parent.add(y);
        temp.addAll(nodes);
        temp.remove(x);
        temp.remove(y);
        temp.add(parent);

        analyze(temp, calculator);
    }

    private List<PrescriptionClusterLeafNode> mLeafNodes = new ArrayList<PrescriptionClusterLeafNode>();
    private PrescriptionClusterCompositeNode mRoot = null;
    private int mCurrentClusterNodeId = 0;
}
