package com.hubert.parser.AST.YiAn.builder;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.YiAn.*;

public class RecipeCompositionChildBuilder implements IYiAnBuilder {

	public RecipeCompositionChildBuilder(YiAnBuilderVisitor visitor, YiAnPrescriptionItemEntity yiAnPrescriptionItemEntity) {

		mYiAnPrescriptionItemEntity = yiAnPrescriptionItemEntity;
	}

	@Override
	public boolean build(ASTNode node) {
		if (YiAnNodeConstants.Herb.equals(node.getTag())) {
			mYiAnPrescriptionItemEntity.herb = node.getValue();
		}
		return false;
	}

	private YiAnPrescriptionItemEntity mYiAnPrescriptionItemEntity;
}
