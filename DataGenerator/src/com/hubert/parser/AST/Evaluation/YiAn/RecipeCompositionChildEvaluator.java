package com.hubert.parser.AST.Evaluation.YiAn;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.*;

public class RecipeCompositionChildEvaluator extends AbstractEvaluator {

    public RecipeCompositionChildEvaluator(Context context) {
        super(YiAnNodeConstants.Herb, context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        BlockCreator<YiAnPrescriptionItemEntity> temp = mYiAnScope.getYiAnPrescriptionItem();
        YiAnPrescriptionItemEntity entity = temp.get();
        entity.herb = node.getValue();
        temp.addToken(node.getSourcePosition());
        return true;
    }
}
