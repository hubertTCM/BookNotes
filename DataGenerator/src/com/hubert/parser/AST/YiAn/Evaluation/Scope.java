package com.hubert.parser.AST.YiAn.Evaluation;

import java.util.*;

import com.hubert.dal.entity.*;

public class Scope {
    public Scope(Context context, Scope parent) {
        mParent = parent;
        mContext = context;
    }

    public Scope(Context context) {
        this(context, null);
    }

    public void destroy() {
        mContext = null;
        mParent = null;
        mVariables.clear();
        mVariables = null;
    }

    public YiAnEntity getYiAn() {
        return getVariable(YiAnKey);
    }

    public void setYiAn(YiAnEntity yiAn) {
        setVariable(YiAnKey, yiAn);
        mContext.addYiAn(yiAn);
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

    @SuppressWarnings("unchecked")
    protected <T> T getVariable(String key) {
        if (mVariables.containsKey(key)) {
            return (T)mVariables.get(key);
        }

        if (mParent != null) {
            return mParent.getVariable(key);
        }
        System.out.println("*** failed to find " + key);
        return null;
    }

    protected <T> T setVariable(String key, T value) {
        if (mVariables.containsKey(key)) {
            // TODO:
        }
        mVariables.put(key, value);
        return value;
    }

    private Map<String, Object> mVariables = new HashMap<String, Object>();
    private Scope mParent;
    private Context mContext;

    private final static String YiAnKey = "YiAn";
    private final static String YiAnDetailKey = "YiAnDetail";
    private final static String YiAnPresciption = "YiAnPrescription";
    private final static String YiAnPrescriptionItem = "YiAnPrescriptionItem";
}
