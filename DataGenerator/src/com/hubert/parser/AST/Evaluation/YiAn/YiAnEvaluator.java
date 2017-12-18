package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
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
        mYiAnScope.setYiAn(yiAn);
        mYiAns.add(yiAn);

        mYiAnScope.initCurrentTokens();
        mYiAnScope.createBlockGroupCreator(BlockGroupTypeEnum.YiAn);
        
        return true;
    }

    private List<YiAnEntity> mYiAns = null;// new ArrayList<YiAnEntity>();
}
