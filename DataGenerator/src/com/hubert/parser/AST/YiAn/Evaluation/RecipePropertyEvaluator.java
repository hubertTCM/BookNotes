package com.hubert.parser.AST.YiAn.Evaluation;

import com.hubert.dal.entity.YiAnPrescriptionEntity;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.YiAnBuilderVisitor;
import com.hubert.parser.AST.YiAn.YiAnNodeConstants;

public class RecipePropertyEvaluator extends AbstractEvaluator {

    public RecipePropertyEvaluator(Context context) {
        super(YiAnNodeConstants.RecipeComment, context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        YiAnPrescriptionEntity yiAnPrescription = mScope.getYiAnPrescription();
        String tag = node.getTag();
        if (YiAnNodeConstants.RecipeComment.equals(tag)) {
            yiAnPrescription.comment = node.getValue();
        }

        return true;
    }
}
