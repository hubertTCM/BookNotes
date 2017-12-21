package com.hubert.machinelearning.agens;

public class LeafNode<T> {
    public LeafNode(T entity) {
        mData = entity;
    }

    public T getValue(){
        return mData;
    }



    // private List<PrescriptionClusterLeafNode> mChildren = new
    // ArrayList<PrescriptionClusterLeafNode>();
    private T mData;
}
