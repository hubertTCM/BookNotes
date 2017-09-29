package com.hubert.parser.AST.YiAn.Evaluation;

import java.util.ArrayList;
import java.util.Arrays;

import com.hubert.dal.entity.YiAnDetailEntity;
import com.hubert.dal.entity.YiAnEntity;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.YiAnNodeConstants;

public class YiAnEvaluator extends AbstractEvaluator {

    public YiAnEvaluator(Context context) {
        super(Arrays.asList(YiAnNodeConstants.YN, YiAnNodeConstants.YN2), context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        YiAnEntity yiAn = new YiAnEntity();
        yiAn.details = new ArrayList<YiAnDetailEntity>();

        mScope.setYiAn(yiAn);
        return true;
    }

}
