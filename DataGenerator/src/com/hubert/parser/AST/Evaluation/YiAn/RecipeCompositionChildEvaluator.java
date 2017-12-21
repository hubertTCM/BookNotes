package com.hubert.parser.AST.Evaluation.YiAn;

import com.hubert.dal.entity.*;
import com.hubert.dto.PrescriptionItem;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.*;

public class RecipeCompositionChildEvaluator extends AbstractEvaluator {

    public RecipeCompositionChildEvaluator(Context context) {
        super(YiAnNodeConstants.Herb, context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        PrescriptionItem entity = mYiAnScope.getYiAnPrescriptionItem();
        entity.herb = node.getValue();
        
        BlockCreator creator = mYiAnScope.getBlockCreator();
        creator.addToken(node.getSourcePosition(), YiAnNodeConstants.RecipeContent);
        return true;
    }
}
