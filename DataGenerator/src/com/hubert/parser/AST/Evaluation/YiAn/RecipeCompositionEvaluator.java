package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.dto.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.*;

public class RecipeCompositionEvaluator extends AbstractEvaluator {

    public RecipeCompositionEvaluator(Context context) {
        super(Arrays.asList(YiAnNodeConstants.RecipeCompositionHerbOnly, YiAnNodeConstants.RecipeContent), context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        PrescriptionItem item = new PrescriptionItem();
        mYiAnScope.setYiAnPrescriptionItem(item);
        

        Prescription prescription = mYiAnScope.getYiAnPrescription();
        Collection<PrescriptionItem> items  = prescription.getItems();
        items.add(item);

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

        BlockCreator creator = mYiAnScope.getBlockCreator();
        if (sibling != null && YiAnNodeConstants.RecipeAbbreviation.equals(sibling.getTag())) {
            creator.addToken(sibling.getSourcePosition(), sibling.getTag());
        }
        return true;
    }

}
