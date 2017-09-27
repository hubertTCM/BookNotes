package com.hubert.parser.AST.YiAn.builder;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.*;

public class RecipeDetailBuilder extends AbstractYiAnBuilder {

    public RecipeDetailBuilder(YiAnBuilderVisitor visitor, YiAnDetailEntity yiAnDetail) {
        super(YiAnNodeConstants.RecipeDetail, visitor);
        mYiAnDetail = yiAnDetail;
    }

    @Override
    protected boolean buildInternal(ASTNode node) {
        mPrescription = new YiAnPrescriptionEntity();
        mPrescription.items = new ArrayList<YiAnPrescriptionItemEntity>();
        mPrescription.order = mYiAnDetail.prescriptions.size();
        mYiAnDetail.prescriptions.add(mPrescription);

        new RecipeCompositionBuilder(mVisitor, mPrescription);
        new RecipePropertyBuilder(mVisitor, mPrescription);
        return true;
    }

    private YiAnDetailEntity mYiAnDetail;
    private YiAnPrescriptionEntity mPrescription;

}
