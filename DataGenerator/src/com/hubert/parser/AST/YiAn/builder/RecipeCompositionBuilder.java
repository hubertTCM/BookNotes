package com.hubert.parser.AST.YiAn.builder;

import java.util.Arrays;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.*;

public class RecipeCompositionBuilder extends AbstractYiAnBuilder {

	public RecipeCompositionBuilder(YiAnBuilderVisitor visitor, YiAnPrescriptionEntity prescriptionEntity) {
		super(Arrays.asList(YiAnNodeConstants.RecipeComposition, YiAnNodeConstants.RecipeContent), visitor);
		mPrescriptionEntity = prescriptionEntity;
		mVisitor = visitor;
	}

	@Override
	public boolean buildInternal(ASTNode node) {
		// TODO Auto-generated method stub
		mPrescriptionItemEntity = new YiAnPrescriptionItemEntity();
		mPrescriptionEntity.items.add(mPrescriptionItemEntity);

		new RecipeCompositionChildBuilder(mVisitor, mPrescriptionItemEntity);

		return true;
	}

	private YiAnBuilderVisitor mVisitor;
	private YiAnPrescriptionEntity mPrescriptionEntity;
	private YiAnPrescriptionItemEntity mPrescriptionItemEntity;

}
