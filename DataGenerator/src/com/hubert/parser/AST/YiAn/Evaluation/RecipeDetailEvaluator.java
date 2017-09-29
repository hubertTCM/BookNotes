package com.hubert.parser.AST.YiAn.Evaluation;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.*;

public class RecipeDetailEvaluator extends AbstractEvaluator {

    public RecipeDetailEvaluator(Context context) {
        super(YiAnNodeConstants.RecipeDetail, context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        YiAnDetailEntity yiAnDetail = mScope.getYiAnDetail();
        YiAnPrescriptionEntity prescription = new YiAnPrescriptionEntity();
        prescription.items = new ArrayList<YiAnPrescriptionItemEntity>();
        prescription.order = yiAnDetail.prescriptions.size();
        yiAnDetail.prescriptions.add(prescription);
        
        mScope.setYiAnPrescription(prescription);
        return true;
    }

}
