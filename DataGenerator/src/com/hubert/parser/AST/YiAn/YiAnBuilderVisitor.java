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
        context.setGlobalData(YiAnScope.YiAnDataProviderKey, dataProvider);
        context.setGlobalData(YiAnScope.HerbAliasManagerKey, herbAliasManager);
        context.setGlobalData(YiAnScope.OriginalTokenKey, mTokens);
        context.setGlobalData(YiAnScope.RootSectionKey, mParentSection);

        mEvaluators.add(new YiAnEvaluator(context, mYiAns));
        mEvaluators.add(new YiAnDetailEvaluator(context));
        mEvaluators.add(new YiAnDetailPropertyEvaluator(context));
        mEvaluators.add(new RecipeCompositionEvaluator(context));
        mEvaluators.add(new RecipeCompositionChildEvaluator(context));
        mEvaluators.add(new RecipeDetailEvaluator(context));
        mEvaluators.add(new RecipePropertyEvaluator(context));
    }

    public YiAnBuilderVisitor(HerbAliasManager herbAliasManager, DataProvider dataProvider) {
        this(null, herbAliasManager, dataProvider);
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
        }

    }

    public void AddYiAn(YiAnEntity yiAn) {
        mYiAns.add(yiAn);
    }

    public List<YiAnEntity> getYiAns() {
        return mYiAns;
    }
    
    public List<SortedMap<Position, String>> getTokens(){
        return mTokens;
    }

    private List<IEvaluator> mEvaluators = new ArrayList<IEvaluator>();
    private List<YiAnEntity> mYiAns = new ArrayList<YiAnEntity>();
    private List<SortedMap<Position, String>> mTokens = new ArrayList<SortedMap<Position, String>>();
    private SectionEntity mParentSection;
    private BookEntity mBook;
}
