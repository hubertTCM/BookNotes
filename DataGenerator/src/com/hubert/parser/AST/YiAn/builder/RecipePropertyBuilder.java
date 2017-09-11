package com.hubert.parser.AST.YiAn.builder;

import com.hubert.dal.entity.YiAnPrescriptionEntity;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.YiAnBuilderVisitor;
import com.hubert.parser.AST.YiAn.YiAnNodeConstants;

public class RecipePropertyBuilder extends AbstractYiAnBuilder {

	public RecipePropertyBuilder(String tag, YiAnBuilderVisitor visitor, YiAnPrescriptionEntity prescriptionEntity) {
		super(tag, visitor);
		mYiAnPrescription = prescriptionEntity;
	}

	@Override
	protected boolean buildInternal(ASTNode node) {
		String tag = node.getTag();
		if (YiAnNodeConstants.RecipeComment.equals(tag)){
			mYiAnPrescription.comment = node.getValue();
		}
		
		return true;
	}

	private YiAnPrescriptionEntity mYiAnPrescription;
}
