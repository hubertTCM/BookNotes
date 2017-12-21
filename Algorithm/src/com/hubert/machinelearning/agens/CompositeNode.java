package com.hubert.machinelearning.agens;

import java.util.*;

public class CompositeNode<T> {
    public CompositeNode(int id) {
        mId = id;
    }

//    public Set<T> getCenter() {
//        if (mCenter == null) {
//            initLeafNodes();
//            List<Set<T>> allHerbs = new ArrayList<Set<T>>();
//            for (LeafNode<T> leaf : mLeafNodes) {
//                //allHerbs.add(leaf.getValue()); // TODO:
//            }
//            CenterCalculator<T> c = new CenterCalculator<T>(allHerbs);
//            mCenter = c.getCenter();
//
//        }
//
//        return mCenter;
//    }

    public int getId() {
        return mId;
    }

    public void add(CompositeNode<T> child) {
        mChildCompositeNodes.add(child);
    }

    public void add(LeafNode<T> node) {
        mLeafNodes.add(node);
    }

    public List<CompositeNode<T>> getCompositeNodes() {
        return mChildCompositeNodes;
    }

    public List<LeafNode<T>> getLeafNodes() {
        initLeafNodes();
        return mLeafNodes;
    }

    private void initLeafNodes() {
        if (!mLeafNodes.isEmpty()) {
            return;
        }

        for (CompositeNode<T> node : mChildCompositeNodes) {
            mLeafNodes.addAll(node.getLeafNodes());
        }

    }

    private List<LeafNode<T>> mLeafNodes = new ArrayList<LeafNode<T>>();

    private List<CompositeNode<T>> mChildCompositeNodes = new ArrayList<CompositeNode<T>>();

    //private Set<T> mCenter;

    private int mId;
}
