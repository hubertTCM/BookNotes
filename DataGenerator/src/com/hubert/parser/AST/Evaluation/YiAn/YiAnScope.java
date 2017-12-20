package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.parser.AST.Evaluation.Common.*;
import com.hubert.parser.AST.YiAn.YiAnNodeConstants;
import com.hubert.parser.tokenextractor.*;

public class YiAnScope {
    public YiAnScope(Storage storage) {
        mStorage = storage;
    }

    public DataProvider getDataProvider() {
        return getVariable(YiAnScope.RawContentProvider);
    }

    public HerbAliasManager getHerbAliasManager() {
        return getVariable(YiAnScope.HerbAliasManagerKey);
    }

    public SectionEntity getRootSection() {
        return getVariable(YiAnScope.RootSectionKey);
    }

    public SectionEntity getActiveSection() {
        SectionEntity section = getVariable(YiAnScope.ActiveSectionEntityKey);
        if (section != null) {
            return section;
        }
        return getRootSection();
    }

    public BlockPositionManager getBlockPositionManager() {
        return getVariable(YiAnScope.BlockPositionManagerKey);
    }

    public BookEntity getBook() {
        return getVariable(YiAnScope.BookKey);
    }

//    public YiAnEntity getYiAn() {
//        return getVariable(YiAnKey);
//    }

//    public void setYiAn(YiAnEntity yiAn) {
//        setRawVariable(YiAnKey, yiAn);
//    }

//    public BlockLinkCreator<YiAnDetailEntity> getYiAnDetail() {
//        return getVariable(YiAnDetailKey);
//    }
//
//    public void setYiAnDetail(YiAnDetailEntity value) {
//        setVariable(YiAnDetailKey, value, YiAnNodeConstants.Description);
//    }

    public YiAnPrescriptionEntity getYiAnPrescription() {
        return getVariable(YiAnPresciption);
    }

    public YiAnPrescriptionEntity setYiAnPrescription(YiAnPrescriptionEntity value) {
        List<YiAnPrescriptionEntity> prescriptions = getPrescriptions();
        prescriptions.add(value);
        return setRawVariable(YiAnPresciption, value);
    }

    public YiAnPrescriptionItemEntity getYiAnPrescriptionItem() {
        return getVariable(YiAnPrescriptionItem);
    }

    public void setYiAnPrescriptionItem(YiAnPrescriptionItemEntity value) {
        setRawVariable(YiAnPrescriptionItem, value);
    }

    public List<SortedMap<Position, String>> getOriginalTokens() {
        return getVariable(YiAnScope.OriginalTokenKey);
    }

    public void initCurrentTokens() {
        SortedMap<Position, String> tokens = new TreeMap<Position, String>();
        setRawVariable(YiAnScope.YiAnTokens, tokens);

        List<SortedMap<Position, String>> all = getOriginalTokens();
        all.add(tokens);
    }

    public SortedMap<Position, String> getCurrentTokens() {
        return getVariable(YiAnScope.YiAnTokens);
    }

    public BlockGroupCreator getBlockGroupCreator() {
        return getVariable(BlockGroupCreatorKey);
    }

    public BlockGroupCreator createBlockGroupCreator(BlockGroupTypeEnum type) {
        BlockGroupCreator creator = new BlockGroupCreator(getBlockGroupCreator(), type, getBlockPositionManager(),
                getCurrentTokens());

        setRawVariable(BlockGroupCreatorKey, creator);
        return creator;
    }

    public BlockCreator createBlockCreator(BlockTypeEnum type) {
        BlockCreator creator = new BlockCreator(type, getDataProvider(), getBlockPositionManager(), getActiveSection());

        BlockGroupCreator blockGroupCreator = getBlockGroupCreator();
        blockGroupCreator.add(creator);

        setRawVariable(BlockCreatorKey, creator);
        return creator;
    }

    public BlockCreator getBlockCreator() {
        return getVariable(BlockCreatorKey);
    }

    public List<BlockGroupEntity> getBlockGroups() {
        return getVariable(BlockGroupKey);
    }

    protected List<YiAnPrescriptionEntity> getPrescriptions() {
        return getVariable(PrescriptionKey);
    }

    protected <T> T getVariable(String key) {
        return mStorage.getVariable(key);
    }

    protected <T> T setRawVariable(String key, T value) {
        return mStorage.setVariable(key, value);
    }

    protected <T> BlockLinkCreator<T> setVariable(String key, T value, String blockType) {
        BlockLinkCreator<T> provider = new BlockLinkCreator<>(value, blockType, this);
        mStorage.setVariable(key, provider).getEntity();
        return provider;
    }

    public final static String RawContentProvider = "Global.RawContentProvider";
    public final static String HerbAliasManagerKey = "Global.HerbAliasManager";
    public final static String OriginalTokenKey = "Global.OriginalToken";
    public final static String RootSectionKey = "Global.RootSection";
    public final static String BookKey = "Global.Book";
    public final static String ActiveSectionEntityKey = "Global.SectionEntity";
    public final static String BlockPositionManagerKey = "Global.BlockPositionManager";

    public final static String BlockGroupKey = "Global.BlockGroup";
    public final static String PrescriptionKey = "Global.PrescriptionKey";

    private Storage mStorage;

    private final static String YiAnKey = "YiAn";
    private final static String YiAnDetailKey = "YiAnDetail"; // TODO: delete
    private final static String YiAnPresciption = "YiAnPrescription";
    private final static String YiAnPrescriptionItem = "YiAnPrescriptionItem";
    private final static String YiAnTokens = "YiAnTokens";

    // private final static String YiAnBlockGroupKey = "YiAnBlockGroup";
    // private final static String YiAnDetailBlockGroupKey =
    // "YiAnDetailBlockGroup";

    private final static String BlockGroupCreatorKey = "BlockGroupCreator";
    private final static String BlockCreatorKey = "BlockCreatorKey";
}
