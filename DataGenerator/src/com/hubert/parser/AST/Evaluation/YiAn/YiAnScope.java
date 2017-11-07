package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

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
        BlockGenerator<SectionEntity> temp = getVariable(SectionEntityKey);
        return temp.get();
    }
    
    public void setSection(SectionEntity section){
        setRawVariable(SectionEntityKey, section);
    }

    public BlockGenerator<YiAnEntity> getYiAn() {
        return getVariable(YiAnKey);
    }

    public void setYiAn(YiAnEntity yiAn) {
        setVariable(YiAnKey, yiAn);
    }
    
    public BlockGenerator<YiAnDetailEntity> getYiAnDetail(){
        return getVariable(YiAnDetailKey);
    }
    
    public void setYiAnDetail(YiAnDetailEntity value){
        setVariable(YiAnDetailKey, value);
    }

    public BlockGenerator<YiAnPrescriptionEntity> getYiAnPrescription() {
        return getVariable(YiAnPresciption);
    }

    public void setYiAnPrescription(YiAnPrescriptionEntity value) {
        setVariable(YiAnPresciption, value);
    }
    
    public BlockGenerator<YiAnPrescriptionItemEntity> getYiAnPrescriptionItem(){
        return getVariable(YiAnPrescriptionItem);
    }
    
    public void setYiAnPrescriptionItem(YiAnPrescriptionItemEntity value){
        setVariable(YiAnPrescriptionItem, value);
    }
    
    public String getNodeType(){
        return getVariable(NodeType);
    }
    
    public void setTokenType(String value){
        setRawVariable(NodeType, value);
    }
    
    public SortedMap<Position, String> getOriginalTokens(){
        return getVariable(YiAnScope.OriginalTokenKey);
    }

    protected <T> T getVariable(String key) {
        return mStorage.getVariable(key);
    }

    protected <T> T setRawVariable(String key, T value) {
        return mStorage.setVariable(key, value);
    }
    
    protected <T> void setVariable(String key, T value) {
        BlockGenerator<T> provider = new BlockGenerator<>(value, this);
        mStorage.setVariable(key, provider).get();
    }

    
    public final static String YiAnDataProviderKey = "Global.DataProvider";
    public final static String HerbAliasManagerKey = "Global.HerbAliasManager";
    public final static String OriginalTokenKey = "Global.OriginalToken";
    
    private Storage mStorage;

    private final static String SectionEntityKey = "SectionEntity";
    private final static String YiAnKey = "YiAn";
    private final static String YiAnDetailKey = "YiAnDetail";
    private final static String YiAnPresciption = "YiAnPrescription";
    private final static String YiAnPrescriptionItem = "YiAnPrescriptionItem";
    
    private final static String NodeType = "NodeType";
    
}
