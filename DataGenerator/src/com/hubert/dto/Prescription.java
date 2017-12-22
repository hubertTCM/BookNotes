package com.hubert.dto;

import java.util.*;

import com.hubert.dal.entity.*;

public class Prescription {
    public Prescription() {
        mPrescriptionEntity = new BookReferenceEntity();
        mItems = new Vector<PrescriptionItem>();
    }

    // 桂枝 白芍 甘草 生姜 大枣
    // cache the composition, easy to analyze
    public String getSummary() {
        return mPrescriptionEntity.summary;
    }

    public void setSummary(String value) {
        mPrescriptionEntity.summary = value;
    }

    public Collection<PrescriptionItem> getItems() {
        return mItems;
    };

    public Set<String> getHerbs() {
        if (!mHerbs.isEmpty()) {
            return mHerbs;
        }

        String[] herbs = mPrescriptionEntity.summary.split(" ");
        mHerbs.addAll(Arrays.asList(herbs));
        return mHerbs;
    }

    public BookReferenceEntity getEntity() {
        return mPrescriptionEntity;
    }

    private BookReferenceEntity mPrescriptionEntity;
    private Collection<PrescriptionItem> mItems;
    private Set<String> mHerbs = new HashSet<String>();
}
