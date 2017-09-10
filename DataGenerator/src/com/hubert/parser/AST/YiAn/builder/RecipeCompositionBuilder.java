package com.hubert.parser.AST.YiAn.builder;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.YiAn.*;

public class RecipeCompositionBuilder extends AbstractYiAnBuilder {

	public RecipeCompositionBuilder(YiAnBuilderVisitor visitor, YiAnPrescriptionItemEntity yiAnPrescriptionItemEntity) {
		super(YiAnNodeConstants.RecipeComposition, visitor);
		mYiAnPrescriptionItemEntity = yiAnPrescriptionItemEntity;
	}

	@Override
	protected boolean buildInternal(ASTNode node) {
		if (YiAnNodeConstants.Herb.equals(node.getTag())) {
			mYiAnPrescriptionItemEntity.herb = node.getValue();
		}
		return false;
	}

	private YiAnPrescriptionItemEntity mYiAnPrescriptionItemEntity;
}
