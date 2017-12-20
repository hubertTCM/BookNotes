package com.hubert.parser.AST.YiAn;

//import java.io.*;
//import java.nio.file.Paths;
import java.util.*;

//import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.IVisitor;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.Evaluation.YiAn.*;
import com.hubert.parser.tokenextractor.*;

public class YiAnBuilderVisitor implements IVisitor {
    public YiAnBuilderVisitor(SectionEntity parentSection, HerbAliasManager herbAliasManager,
            DataProvider dataProvider) {

        mParentSection = parentSection;

        SectionEntity currentSection = parentSection;
        while (mBook == null && currentSection != null) {
            mBook = currentSection.book;
            currentSection = currentSection.parent;
        }

        Context context = new Context();
        context.setGlobalData(YiAnScope.RawContentProvider, dataProvider);
        context.setGlobalData(YiAnScope.HerbAliasManagerKey, herbAliasManager);
        context.setGlobalData(YiAnScope.OriginalTokenKey, mTokens);
        context.setGlobalData(YiAnScope.RootSectionKey, mParentSection);
        context.setGlobalData(YiAnScope.BookKey, mBook);
        context.setGlobalData(YiAnScope.BlockPositionManagerKey, mBlockPositionManager);
        context.setGlobalData(YiAnScope.BlockGroupKey, mBlockGroups);
        context.setGlobalData(YiAnScope.PrescriptionKey, mPrescriptions);

        mEvaluators.add(new SectionNameEvaluator(context));
        mEvaluators.add(new YiAnEvaluator(context));
        mEvaluators.add(new YiAnDetailEvaluator(context));
        mEvaluators.add(new YiAnDetailPropertyEvaluator(context));
        mEvaluators.add(new RecipeCompositionEvaluator(context));
        mEvaluators.add(new RecipeCompositionChildEvaluator(context));
        mEvaluators.add(new RecipeDetailEvaluator(context));
        mEvaluators.add(new RecipePropertyEvaluator(context));
        mEvaluators.add(new CommentEvaluator(context));
    }


    @Override
    public void visit(ASTNode node) {
        // System.out.println(node.getTag());
        IEvaluator evaluator = null;
        for (IEvaluator temp : mEvaluators) {
            if (temp.canEvaluate(node)) {
                evaluator = temp;
                evaluator.evaluate(node);
                break;
            }
        }

        int childCount = node.childCount();
        for (int i = 0; i < childCount; ++i) {
            node.getChild(i).accept(this);
        }

        if (evaluator != null) {
            evaluator.postEvaluate(node);
            evaluator.clear();
        }
    }
    
    public List<BlockGroupEntity> getBlockGroups(){
        return mBlockGroups;
    }
    
    public List<PrescriptionEntity> getPrescriptions(){
        sortBlocks(mParentSection);
        return mPrescriptions;
    }
    
    public List<SortedMap<Position, String>> getTokens(){
        return mTokens;
    }
    private void sortBlocks(SectionEntity section){
        List<BlockEntity> temp = new Vector<BlockEntity>();
        temp.addAll(section.blocks);
        temp.sort(new Comparator<BlockEntity>(){

            @Override
            public int compare(BlockEntity o1, BlockEntity o2) {
                Position x = mBlockPositionManager.getPosition(o1);
                Position y = mBlockPositionManager.getPosition(o2);
                return x.compareTo(y);
            }});
        section.blocks.clear();
        
        for(int i = 0; i < temp.size(); i++){
            BlockEntity entity = temp.get(i);
            entity.order = i+ 1;
            section.blocks.add(entity);
        }
       
        for(SectionEntity child : section.childSections){
            sortBlocks(child);
        }
    }

    private List<IEvaluator> mEvaluators = new ArrayList<IEvaluator>();
    private List<PrescriptionEntity> mPrescriptions = new Vector<PrescriptionEntity>();
    private List<BlockGroupEntity> mBlockGroups = new Vector<BlockGroupEntity>();
    private List<SortedMap<Position, String>> mTokens = new ArrayList<SortedMap<Position, String>>();
    private BlockPositionManager mBlockPositionManager = new BlockPositionManager();
    private SectionEntity mParentSection;
    private BookEntity mBook;
}
