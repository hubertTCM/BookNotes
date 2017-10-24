package com.hubert.parser.AST.YiAn.Evaluation;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.Evaluation.Common.*;

public class Scope {
    public Scope(Storage storage) {
        mStorage = storage;
    }

    public YiAnEntity getYiAn() {
        return getVariable(YiAnKey);
    }

    public void setYiAn(YiAnEntity yiAn) {
        setVariable(YiAnKey, yiAn);
    }
    
    public YiAnDetailEntity getYiAnDetail(){
        return getVariable(YiAnDetailKey);
    }
    
    public YiAnDetailEntity setYiAnDetail(YiAnDetailEntity value){
        return setVariable(YiAnDetailKey, value);
    }

    public YiAnPrescriptionEntity getYiAnPrescription() {
        return getVariable(YiAnPresciption);
    }

    public YiAnPrescriptionEntity setYiAnPrescription(YiAnPrescriptionEntity value) {
        setVariable(YiAnPresciption, value);
        return value;
    }
    
    public YiAnPrescriptionItemEntity getYiAnPrescriptionItem(){
        return getVariable(YiAnPrescriptionItem);
    }
    
    public YiAnPrescriptionItemEntity setYiAnPrescriptionItem(YiAnPrescriptionItemEntity value){
        return setVariable(YiAnPrescriptionItem, value);
    }

    protected <T> T getVariable(String key) {
        return mStorage.getVariable(key);
    }

    protected <T> T setVariable(String key, T value) {
        return mStorage.setVariable(key, value);
    }

    private Storage mStorage;

    private final static String YiAnKey = "YiAn";
    private final static String YiAnDetailKey = "YiAnDetail";
    private final static String YiAnPresciption = "YiAnPrescription";
    private final static String YiAnPrescriptionItem = "YiAnPrescriptionItem";
}
