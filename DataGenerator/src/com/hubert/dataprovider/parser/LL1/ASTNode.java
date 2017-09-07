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
	
	public int getChildCount(){
		return mChildren.size();
	}
	
	public void addChild(int index, ASTNode child){
		mChildren.add(index, child);
		if (child.mParent != null) {
			// TODO:
		}
		child.mParent = this;
	}

	public void addChild(ASTNode child) {
		int index  = mChildren.size();
		addChild(index, child);
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
