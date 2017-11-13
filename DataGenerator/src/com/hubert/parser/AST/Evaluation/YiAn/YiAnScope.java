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

    public SectionEntity getRootSection(){
        return getVariable(YiAnScope.RootSectionKey);
    }

    public SectionEntity getActiveSection(){
        return getVariable(YiAnScope.ActiveSectionEntityKey);
    }
    
    public BookEntity getBook(){
        return getVariable(YiAnScope.BookKey);
    }
//    
//    public void setSection(SectionEntity section){
//        setRawVariable(SectionEntityKey, section);
//    }

    public BlockCreator<YiAnEntity> getYiAn() {
        return getVariable(YiAnKey);
    }

    public void setYiAn(YiAnEntity yiAn) {
        setVariable(YiAnKey, yiAn);
    }
    
    public BlockCreator<YiAnDetailEntity> getYiAnDetail(){
        return getVariable(YiAnDetailKey);
    }
    
    public void setYiAnDetail(YiAnDetailEntity value){
        setVariable(YiAnDetailKey, value);
    }

    public BlockCreator<YiAnPrescriptionEntity> getYiAnPrescription() {
        return getVariable(YiAnPresciption);
    }

    public BlockCreator<YiAnPrescriptionEntity> setYiAnPrescription(YiAnPrescriptionEntity value) {
        return setVariable(YiAnPresciption, value);
    }
    
    public BlockCreator<YiAnPrescriptionItemEntity> getYiAnPrescriptionItem(){
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
    
    public List<SortedMap<Position, String>> getOriginalTokens(){
        return getVariable(YiAnScope.OriginalTokenKey);
    }
    
    public void initCurrentTokens(){
        SortedMap<Position, String> tokens = new TreeMap<Position, String>();
        setRawVariable(YiAnScope.YiAnTokens, tokens);
        
        List<SortedMap<Position, String>> all = getOriginalTokens();
        all.add(tokens);
    }
    
    public SortedMap<Position, String> getCurrentTokens(){
        return getVariable(YiAnScope.YiAnTokens);
    }

    protected <T> T getVariable(String key) {
        return mStorage.getVariable(key);
    }

    protected <T> T setRawVariable(String key, T value) {
        return mStorage.setVariable(key, value);
    }
    
    protected <T> BlockCreator<T> setVariable(String key, T value) {
        BlockCreator<T> provider = new BlockCreator<>(value, this);
        mStorage.setVariable(key, provider).get();
        return provider;
    }

    
    public final static String YiAnDataProviderKey = "Global.DataProvider";
    public final static String HerbAliasManagerKey = "Global.HerbAliasManager";
    public final static String OriginalTokenKey = "Global.OriginalToken";
    public final static String RootSectionKey = "Global.RootSection";
    public final static String BookKey = "Global.Book";
    public final static String ActiveSectionEntityKey = "Global.SectionEntity";
    
    private Storage mStorage;

    private final static String YiAnKey = "YiAn";
    private final static String YiAnDetailKey = "YiAnDetail";
    private final static String YiAnPresciption = "YiAnPrescription";
    private final static String YiAnPrescriptionItem = "YiAnPrescriptionItem";
    private final static String YiAnTokens = "YiAnTokens";
    
    private final static String NodeType = "NodeType";
    
}
