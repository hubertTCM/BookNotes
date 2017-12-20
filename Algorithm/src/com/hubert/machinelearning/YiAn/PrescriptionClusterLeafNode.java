package com.hubert.machinelearning.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;

public class PrescriptionClusterLeafNode {
    public PrescriptionClusterLeafNode(PrescriptionEntity entity) {
        mYiAnPrescriptionEntity = entity;
        String[] herbs = mYiAnPrescriptionEntity.summary.split(" ");
        mHerbs.addAll(Arrays.asList(herbs));
    }

    public Set<String> getHerbs() {
        return mHerbs;
    }

    public String getSummary() {
        return mYiAnPrescriptionEntity.summary;
    }

    // private List<PrescriptionClusterLeafNode> mChildren = new
    // ArrayList<PrescriptionClusterLeafNode>();
    private PrescriptionEntity mYiAnPrescriptionEntity;
    private Set<String> mHerbs = new HashSet<String>();
}
