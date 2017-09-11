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

		RecipeCompositionBuilder builder = new RecipeCompositionBuilder(mVisitor, mPrescription);
		mVisitor.registerBuilder(YiAnNodeConstants.RecipeComposition, builder);
		mVisitor.registerBuilder(YiAnNodeConstants.RecipeContent, builder);

		RecipePropertyBuilder propertyBuilder = new RecipePropertyBuilder(YiAnNodeConstants.RecipeComment, mVisitor,
				mPrescription);
		mVisitor.registerBuilder(YiAnNodeConstants.RecipeComment, propertyBuilder);
		return true;
	}

	private YiAnDetailEntity mYiAnDetail;
	private YiAnPrescriptionEntity mPrescription;

}
