package com.hubert.parser.AST.Evaluation.YiAn;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.parser.AST.Evaluation.Common.*;
import com.hubert.parser.tokenextractor.*;

public class YiAnScope {
    public YiAnScope(Storage storage) {
        mStorage = storage;
    }
    
    public DataProvider getDataProvider(){
        return getVariable(YiAnScope.YiAnDataProviderKey);
    }
    
    public HerbAliasManager getHerbAliasManager(){
        return getVariable(YiAnScope.HerbAliasManagerKey);
    }

    public SectionEntity getSection(){
        EntityProvider<SectionEntity> temp = getVariable(SectionEntityKey);
        return temp.get();
    }
    
    public void setSection(SectionEntity section){
        setRawVariable(SectionEntityKey, section);
    }

    public EntityProvider<YiAnEntity> getYiAn() {
        return getVariable(YiAnKey);
    }

    public void setYiAn(YiAnEntity yiAn) {
        setVariable(YiAnKey, yiAn);
    }
    
    public EntityProvider<YiAnDetailEntity> getYiAnDetail(){
        return getVariable(YiAnDetailKey);
    }
    
    public void setYiAnDetail(YiAnDetailEntity value){
        setVariable(YiAnDetailKey, value);
    }

    public EntityProvider<YiAnPrescriptionEntity> getYiAnPrescription() {
        return getVariable(YiAnPresciption);
    }

    public void setYiAnPrescription(YiAnPrescriptionEntity value) {
        setVariable(YiAnPresciption, value);
    }
    
    public EntityProvider<YiAnPrescriptionItemEntity> getYiAnPrescriptionItem(){
        return getVariable(YiAnPrescriptionItem);
    }
    
    public void setYiAnPrescriptionItem(YiAnPrescriptionItemEntity value){
        setVariable(YiAnPrescriptionItem, value);
    }

    protected <T> T getVariable(String key) {
        return mStorage.getVariable(key);
    }

    protected <T> T setRawVariable(String key, T value) {
        return mStorage.setVariable(key, value);
    }
    
    protected <T> void setVariable(String key, T value) {
        EntityProvider<T> provider = new EntityProvider<>(value);
        mStorage.setVariable(key, provider).get();
    }

    
    public final static String YiAnDataProviderKey = "DataProvider";
    public final static String HerbAliasManagerKey = "HerbAliasManager";
    
    private Storage mStorage;

    private final static String SectionEntityKey = "SectionEntity";
    private final static String YiAnKey = "YiAn";
    private final static String YiAnDetailKey = "YiAnDetail";
    private final static String YiAnPresciption = "YiAnPrescription";
    private final static String YiAnPrescriptionItem = "YiAnPrescriptionItem";
    
}
