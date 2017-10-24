package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hubert.dal.entity.YiAnDetailEntity;
import com.hubert.dal.entity.YiAnEntity;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.YiAnNodeConstants;

public class YiAnEvaluator extends AbstractEvaluator {

    public YiAnEvaluator(Context context, List<YiAnEntity> yiAns) {
        super(Arrays.asList(YiAnNodeConstants.YN, YiAnNodeConstants.YN2), context);
        mYiAns = yiAns;
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        YiAnEntity yiAn = new YiAnEntity();
        yiAn.details = new ArrayList<YiAnDetailEntity>();

        mScope.setYiAn(yiAn);
        mYiAns.add(yiAn);
        return true;
    }

    private List<YiAnEntity> mYiAns = null;//new ArrayList<YiAnEntity>();
}
