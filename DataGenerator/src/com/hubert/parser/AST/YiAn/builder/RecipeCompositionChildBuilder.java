package com.hubert.parser.AST.YiAn.builder;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.YiAn.*;

public class RecipeCompositionChildBuilder extends AbstractYiAnBuilder {

    public RecipeCompositionChildBuilder(YiAnBuilderVisitor visitor,
            YiAnPrescriptionItemEntity yiAnPrescriptionItemEntity) {
        super(YiAnNodeConstants.Herb, visitor);
        mYiAnPrescriptionItemEntity = yiAnPrescriptionItemEntity;
    }

    @Override
    public boolean buildInternal(ASTNode node) {
        if (YiAnNodeConstants.Herb.equals(node.getTag())) {
            mYiAnPrescriptionItemEntity.herb = node.getValue();
        }
        return false;
    }

    private YiAnPrescriptionItemEntity mYiAnPrescriptionItemEntity;
}
