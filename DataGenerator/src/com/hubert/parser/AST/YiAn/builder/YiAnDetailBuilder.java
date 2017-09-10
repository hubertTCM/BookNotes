package com.hubert.parser.AST.YiAn.builder;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.*;

public class YiAnDetailBuilder extends AbstractYiAnBuilder {
	public YiAnDetailBuilder(YiAnBuilderVisitor visitor, YiAnEntity yiAn) {
		super(YiAnNodeConstants.YiAnDetail, visitor);
	}

	@Override
	protected boolean buildInternal(ASTNode node) {
		mYiAnDetail = new YiAnDetailEntity();
		mYiAnDetail.order = mYiAn.details.size() + 1;
		mYiAn.details.add(mYiAnDetail);
		return false;
	}
	
	private YiAnEntity mYiAn;
	private YiAnDetailEntity mYiAnDetail;

}
