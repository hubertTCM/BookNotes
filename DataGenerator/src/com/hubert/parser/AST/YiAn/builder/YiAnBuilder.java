package com.hubert.parser.AST.YiAn.builder;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.YiAnBuilderVisitor;
import com.hubert.parser.AST.YiAn.YiAnNodeConstants;

public class YiAnBuilder extends AbstractYiAnBuilder {

	public YiAnBuilder(YiAnBuilderVisitor visitor) {
		super(YiAnNodeConstants.YN, visitor);
	}

	@Override
	protected boolean buildInternal(ASTNode node) {
		YiAnEntity mYiAn = new YiAnEntity();
		mYiAn.details = new ArrayList<YiAnDetailEntity>();
		mVisitor.AddYiAn(mYiAn);

		new YiAnDetailBuilder(mVisitor, mYiAn);
		return true;
	}

}
