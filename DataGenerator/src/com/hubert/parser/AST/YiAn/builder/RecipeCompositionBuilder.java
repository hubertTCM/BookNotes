package com.hubert.parser.AST.YiAn.builder;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.*;

public class RecipeCompositionBuilder implements IYiAnBuilder {

	public RecipeCompositionBuilder(YiAnBuilderVisitor visitor, YiAnPrescriptionEntity prescriptionEntity) {
		//super(YiAnNodeConstants.RecipeComposition, visitor);
		mPrescriptionEntity = prescriptionEntity;
		mVisitor = visitor;
	}

	@Override
	public boolean build(ASTNode node) {
		// TODO Auto-generated method stub
		mPrescriptionItemEntity = new YiAnPrescriptionItemEntity();
		mPrescriptionEntity.items.add(mPrescriptionItemEntity);

		RecipeCompositionChildBuilder builder = new RecipeCompositionChildBuilder(mVisitor, mPrescriptionItemEntity);
		mVisitor.registerBuilder(YiAnNodeConstants.Herb, builder);
		
		return true;
	}
	private YiAnBuilderVisitor mVisitor;
	private YiAnPrescriptionEntity mPrescriptionEntity;
	private YiAnPrescriptionItemEntity mPrescriptionItemEntity;

}
