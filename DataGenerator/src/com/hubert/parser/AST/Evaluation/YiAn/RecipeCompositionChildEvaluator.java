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
        YiAnPrescriptionItemEntity entity = mYiAnScope.getYiAnPrescriptionItem();
        entity.herb = node.getValue();
        
        BlockLinkCreator<YiAnPrescriptionEntity> prescriptionBlockCreator = mYiAnScope.getYiAnPrescription();
        prescriptionBlockCreator.addToken(node.getSourcePosition(), YiAnNodeConstants.RecipeContent);
        
        BlockCreator creator = mYiAnScope.getBlockCreator();
        creator.addToken(node.getSourcePosition(), YiAnNodeConstants.RecipeContent);
        return true;
    }
}
