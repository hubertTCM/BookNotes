package com.hubert.machinelearning.YiAn;

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
