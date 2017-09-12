package com.hubert.parser.AST.YiAn.builder;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.*;

public class YiAnDetailBuilder extends AbstractYiAnBuilder {
	public YiAnDetailBuilder(YiAnBuilderVisitor visitor, YiAnEntity yiAn) {
		super(Arrays.asList(YiAnNodeConstants.YiAnDetail, YiAnNodeConstants.YNDetail2), visitor);
		mYiAn = yiAn;
	}

	@Override
	protected boolean buildInternal(ASTNode node) {
		mYiAnDetail = new YiAnDetailEntity();
		mYiAnDetail.prescriptions = new ArrayList<YiAnPrescriptionEntity>();
		mYiAnDetail.order = mYiAn.details.size() + 1;
		mYiAn.details.add(mYiAnDetail);

		new RecipeDetailBuilder(mVisitor, mYiAnDetail);

		new YiAnDetailPropertyBuilder(mVisitor, mYiAnDetail);
		return true;
	}

	private YiAnEntity mYiAn;
	private YiAnDetailEntity mYiAnDetail;

}
