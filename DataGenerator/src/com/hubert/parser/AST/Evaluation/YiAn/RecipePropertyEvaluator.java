package com.hubert.parser.AST.Evaluation.YiAn;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.*;

public class RecipePropertyEvaluator extends AbstractEvaluator {

    public RecipePropertyEvaluator(Context context) {
        super(YiAnNodeConstants.RecipeComment, context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        YiAnPrescriptionEntity yiAnPrescription = mYiAnScope.getYiAnPrescription();
        String tag = node.getTag();
        if (YiAnNodeConstants.RecipeComment.equals(tag)) {
            yiAnPrescription.comment = node.getValue();
        }

        return true;
    }
}
