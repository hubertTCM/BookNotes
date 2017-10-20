package com.hubert.parser.AST.YiAn;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.IVisitor;
import com.hubert.parser.AST.YiAn.Evaluation.*;

public class YiAnBuilderVisitor implements IVisitor {
    public YiAnBuilderVisitor(SectionEntity parentSection, HerbAliasManager herbAliasManager) {
        this(herbAliasManager);
        mParentSection = parentSection;

        SectionEntity currentSection = parentSection;
        while (mBook == null && currentSection != null) {
            mBook = currentSection.book;
            currentSection = currentSection.parent;
        }
    }

    public YiAnBuilderVisitor(String logFolder, HerbAliasManager herbAliasManager) {
        this(herbAliasManager);
    }

    private YiAnBuilderVisitor(HerbAliasManager herbAliasManager) {
        mHerbAliasManager = herbAliasManager;
        
        Context mContext = new Context(mYiAns);

        mEvaluators.add(new YiAnEvaluator(mContext));
        mEvaluators.add(new YiAnDetailEvaluator(mContext));
        mEvaluators.add(new YiAnDetailPropertyEvaluator(mContext));
        mEvaluators.add(new RecipeCompositionEvaluator(mContext));
        mEvaluators.add(new RecipeCompositionChildEvaluator(mContext));
        mEvaluators.add(new RecipeDetailEvaluator(mContext, mHerbAliasManager));
        mEvaluators.add(new RecipePropertyEvaluator(mContext));
    }

    @Override
    public void visit(ASTNode node) {
        //System.out.println(node.getTag());
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

        //System.out.println(node.getTag() + "*****");
        if (evaluator != null) {
            evaluator.postEvaluate(node);
        }

    }

    // private IYiAnBuilder getBuilder(ASTNode node) {
    // String tag = node.getTag();
    // String key = tag;
    // if (mBuilders.containsKey(key)) {
    // return mBuilders.get(key);
    // }
    // return null;
    // }

    public void AddYiAn(YiAnEntity yiAn) {
        mYiAns.add(yiAn);
    }

    public List<YiAnEntity> getYiAns() {
        return mYiAns;
    }

    private List<IEvaluator> mEvaluators = new ArrayList<IEvaluator>();
    private List<YiAnEntity> mYiAns = new ArrayList<YiAnEntity>();
    private SectionEntity mParentSection;
    private BookEntity mBook;

    private HerbAliasManager mHerbAliasManager;

}
