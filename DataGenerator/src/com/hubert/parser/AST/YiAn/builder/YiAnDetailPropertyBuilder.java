package com.hubert.parser.AST.YiAn.builder;

import com.hubert.dal.entity.YiAnDetailEntity;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.YiAnBuilderVisitor;
import com.hubert.parser.AST.YiAn.YiAnNodeConstants;

public class YiAnDetailPropertyBuilder extends AbstractYiAnBuilder {

	public YiAnDetailPropertyBuilder(YiAnBuilderVisitor visitor, YiAnDetailEntity yiAnDetail) {
		super(YiAnNodeConstants.Description, visitor);
		mYiAnDetail = yiAnDetail;
	}

	@Override
	protected boolean buildInternal(ASTNode node) {
		String tag = node.getTag();
		if (YiAnNodeConstants.Description.equals(tag)) {
			mYiAnDetail.content = node.getValue();
		}
		return true;
	}

	private YiAnDetailEntity mYiAnDetail;
}
