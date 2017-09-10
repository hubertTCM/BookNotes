package com.hubert.parser.AST.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.IVisitor;
import com.hubert.parser.AST.YiAn.builder.*;

public class YiAnBuilderVisitor implements IVisitor {
	public YiAnBuilderVisitor(SectionEntity parentSection) {
		mParentSection = parentSection;
		YiAnBuilder builder = new YiAnBuilder(this);
		registerBuilder(builder.getNodeTag(), builder);
	}

	@Override
	public void visit(ASTNode node) {
		IYiAnBuilder builder = getBuilder(node);
		if (builder != null) {
			builder.build(node);
		}
		int childCount = node.childCount();
		for (int i = 0; i < childCount; ++i) {
			node.getChild(i).accept(this);
		}
	}

	public void registerBuilder(String type, IYiAnBuilder builder) {
		mBuilders.put(type, builder);
	}

	private IYiAnBuilder getBuilder(ASTNode node) {
		String tag = node.getTag();
		String key = tag;
		if (YiAnNodeConstants.RecipeCompositionHerbOnly.equals(tag)) {
			key = YiAnNodeConstants.RecipeComposition;
		}
		if (mBuilders.containsKey(key)) {
			return mBuilders.get(key);
		}
		return null;
	}

	public void AddYiAn(YiAnEntity yiAn) {
		mYiAns.add(yiAn);
	}

	public List<YiAnEntity> getYiAns() {
		return mYiAns;
	}

	private Map<String, IYiAnBuilder> mBuilders = new HashMap<String, IYiAnBuilder>();
	private List<YiAnEntity> mYiAns = new ArrayList<YiAnEntity>();
	private SectionEntity mParentSection;

}
