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
    public YiAnBuilderVisitor(SectionEntity parentSection, HerbAliasManager herbAliasManager, DataProvider dataProvider) {
        this(herbAliasManager, dataProvider);
        mParentSection = parentSection;

        SectionEntity currentSection = parentSection;
        while (mBook == null && currentSection != null) {
            mBook = currentSection.book;
            currentSection = currentSection.parent;
        }
    }

    public YiAnBuilderVisitor(HerbAliasManager herbAliasManager, DataProvider dataProvider) {
        Context context = new Context();
        context.setGlobalData(YiAnScope.YiAnDataProviderKey, dataProvider);
        context.setGlobalData(YiAnScope.HerbAliasManagerKey, herbAliasManager);

        mEvaluators.add(new YiAnEvaluator(context, mYiAns));
        mEvaluators.add(new YiAnDetailEvaluator(context));
        mEvaluators.add(new YiAnDetailPropertyEvaluator(context));
        mEvaluators.add(new RecipeCompositionEvaluator(context));
        mEvaluators.add(new RecipeCompositionChildEvaluator(context));
        mEvaluators.add(new RecipeDetailEvaluator(context));
        mEvaluators.add(new RecipePropertyEvaluator(context));
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
}
