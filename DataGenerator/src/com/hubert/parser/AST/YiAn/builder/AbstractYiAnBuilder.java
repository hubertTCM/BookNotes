package com.hubert.parser.AST.YiAn.builder;

import java.util.*;

import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.YiAn.*;

public abstract class AbstractYiAnBuilder implements IYiAnBuilder {
	protected AbstractYiAnBuilder(String tag, YiAnBuilderVisitor visitor) {	
		this(Arrays.asList(tag), visitor);
	}

	protected AbstractYiAnBuilder(List<String> tags, YiAnBuilderVisitor visitor) {
		mTags.addAll(tags);
		mVisitor = visitor;
		register();
	}

	

	@Override
	public boolean build(ASTNode node) {
		// TODO: check tag here.
		//if (mTags.contains(node.getTag())) {
			return buildInternal(node);
		//}
		//return false;
	}

	protected void register() {
		for (String tag : mTags) {
			mVisitor.registerBuilder(tag, this);
		}
	}

	protected abstract boolean buildInternal(ASTNode node);

	protected List<String> mTags = new ArrayList<String>();
	protected YiAnBuilderVisitor mVisitor;
}
