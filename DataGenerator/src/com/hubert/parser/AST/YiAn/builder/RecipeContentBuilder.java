package com.hubert.parser.AST.YiAn.builder;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.*;

public class RecipeContentBuilder extends AbstractYiAnBuilder {

	public RecipeContentBuilder(YiAnBuilderVisitor visitor, YiAnPrescriptionEntity prescriptionEntity) {
		super(YiAnNodeConstants.RecipeContent, visitor);
		mPrescriptionEntity = prescriptionEntity;
	}

	@Override
	protected boolean buildInternal(ASTNode node) {
		// TODO Auto-generated method stub
		mPrescriptionItemEntity = new YiAnPrescriptionItemEntity();
		mPrescriptionEntity.items.add(mPrescriptionItemEntity);

		RecipeCompositionBuilder builder = new RecipeCompositionBuilder(mVisitor, mPrescriptionItemEntity);
		mVisitor.registerBuilder(builder.getNodeTag(), builder);
		
		return true;
	}
	
	private YiAnPrescriptionEntity mPrescriptionEntity;
	private YiAnPrescriptionItemEntity mPrescriptionItemEntity;

}
