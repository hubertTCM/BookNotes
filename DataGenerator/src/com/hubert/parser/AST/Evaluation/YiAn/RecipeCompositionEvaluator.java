package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.Arrays;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.*;

public class RecipeCompositionEvaluator extends AbstractEvaluator {

    public RecipeCompositionEvaluator(Context context) {
        super(Arrays.asList(YiAnNodeConstants.RecipeComposition, YiAnNodeConstants.RecipeCompositionHerbOnly,
                YiAnNodeConstants.RecipeContent), context);
    }
    
    @Override
    protected boolean evaluateCore(ASTNode node) {
        YiAnPrescriptionEntity prescription = mScope.getYiAnPrescription();
        YiAnPrescriptionItemEntity item = new YiAnPrescriptionItemEntity();
        prescription.items.add(item);
        
        mScope.setYiAnPrescriptionItem(item);
        return true;
    }

   

}
