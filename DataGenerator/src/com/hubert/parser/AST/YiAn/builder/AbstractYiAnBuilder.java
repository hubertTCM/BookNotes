package com.hubert.parser.AST.YiAn.builder;

import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.*;

public abstract class AbstractYiAnBuilder implements IYiAnBuilder {
	public AbstractYiAnBuilder(String tag, YiAnBuilderVisitor visitor) {
		mTag = tag;
		mVisitor = visitor;
	}

	//@Override
	public String getNodeTag() {
		return mTag;
	}

	@Override
	public boolean build(ASTNode node) {
		if (mTag.equals(node.getTag())) {
			return buildInternal(node);
		}
		return false;
	}

	protected abstract boolean buildInternal(ASTNode node);

	protected String mTag;
	protected YiAnBuilderVisitor mVisitor;
}
