package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.Arrays;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.*;

public class RecipePropertyEvaluator extends AbstractEvaluator {

    public RecipePropertyEvaluator(Context context) {
        super(Arrays.asList(YiAnNodeConstants.RecipeComment, YiAnNodeConstants.RecipeHeader), context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        BlockCreator creator = mYiAnScope.getBlockCreator();
        creator.addToken(node.getSourcePosition(), node.getTag());

        return true;
    }
}
