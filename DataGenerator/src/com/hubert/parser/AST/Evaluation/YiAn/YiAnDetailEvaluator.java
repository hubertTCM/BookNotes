package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.*;

public class YiAnDetailEvaluator extends AbstractEvaluator {
    public YiAnDetailEvaluator(Context context) {
        super(Arrays.asList(YiAnNodeConstants.YiAnDetail, YiAnNodeConstants.YNDetail2), context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        YiAnEntity yiAn = mYiAnScope.getYiAn();
        YiAnDetailEntity yiAnDetail = new YiAnDetailEntity();
        yiAnDetail.prescriptions = new ArrayList<YiAnPrescriptionEntity>();
        yiAnDetail.order = yiAn.details.size() + 1;
        yiAn.details.add(yiAnDetail);

        mYiAnScope.setYiAnDetail(yiAnDetail);
        return true;
    }

}