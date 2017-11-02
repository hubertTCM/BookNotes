package com.hubert.parser.AST.Evaluation.YiAn;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.*;

public class YiAnDetailPropertyEvaluator extends AbstractEvaluator {

    public YiAnDetailPropertyEvaluator(Context context) {
        super(YiAnNodeConstants.Description, context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        YiAnDetailEntity mYiAnDetail = mYiAnScope.getYiAnDetail();
        String tag = node.getTag();
        if (YiAnNodeConstants.Description.equals(tag)) {
            mYiAnDetail.content = node.getValue();
        }
        return true;
    }
}
