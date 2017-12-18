package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.Arrays;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.*;

public class RecipeCompositionEvaluator extends AbstractEvaluator {

    public RecipeCompositionEvaluator(Context context) {
        super(Arrays.asList(YiAnNodeConstants.RecipeCompositionHerbOnly, YiAnNodeConstants.RecipeContent), context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        BlockLinkCreator<YiAnPrescriptionEntity> temp = mYiAnScope.getYiAnPrescription();
        YiAnPrescriptionEntity prescription = temp.getEntity();
        YiAnPrescriptionItemEntity item = new YiAnPrescriptionItemEntity();
        prescription.items.add(item);
        mYiAnScope.setYiAnPrescriptionItem(item);

        return true;
    }

    @Override
    protected boolean postEvaluateCore(ASTNode node) {
        ASTNode sibling = null;
        ASTNode parent = node.getParent();
        for (int i = 0; i < parent.childCount(); ++i) {
            ASTNode tempNode = parent.getChild(i);
            if (tempNode == node) {
                break;
            }
            sibling = tempNode;
        }

        BlockLinkCreator<YiAnPrescriptionEntity> prescriptionBlockCreator = mYiAnScope.getYiAnPrescription();
        if (sibling != null && YiAnNodeConstants.RecipeAbbreviation.equals(sibling.getTag())) {
            prescriptionBlockCreator.addToken(sibling.getSourcePosition(), sibling.getTag());
        }
        return true;
    }

}
