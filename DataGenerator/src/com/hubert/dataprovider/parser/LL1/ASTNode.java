package com.hubert.dataprovider.parser.LL1;

import java.util.*;

import com.hubert.dataprovider.parser.tokenextractor.*;

public class ASTNode {
	public ASTNode(String tag) {
		this(tag, "");
	}

	public ASTNode(String tag, String value) {
		mValue = value;
		mTag = tag;
	}

	public String getTag() {
		return mTag;
	}

	public String getValue() {
		return mValue;
	}

	public void setValue(String value) {
		mValue = value;
	}
	
	public ASTNode getParent(){
		return mParent;
	}

	public void addChild(ASTNode child) {
		mChildren.add(child);
		if (child.mParent != null) {
			// TODO:
		}
		child.mParent = this;
	}

	public boolean replaceChild(ASTNode oldNode, ASTNode newNode) {
		if (newNode.mParent != null) {
			return false;
		}
		int index = mChildren.indexOf(oldNode);
		if (index < 0) {
			return false;
		}
		mChildren.remove(index);
		oldNode.mParent = null;

		newNode.mParent = this;
		mChildren.add(index, newNode);
		return true;
	}

	private String mTag;
	private String mValue;
	private List<ASTNode> mChildren = new ArrayList<ASTNode>();
	private ASTNode mParent;
}
