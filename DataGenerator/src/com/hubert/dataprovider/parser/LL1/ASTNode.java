package com.hubert.dataprovider.parser.LL1;

import java.util.*;

import com.hubert.dataprovider.parser.tokenextractor.*;

public class ASTNode {
	public ASTNode(String tag, Token token){
		mTag  = tag;
		mToken = token;
	}
	
	public Token getToken(){
		return mToken;
	}
	
	public String getTag(){
		return mTag;
	}
	
	public void addChild(ASTNode child){
		mChildren.add(child);
		if (child.mParent != null){
			// TODO:
		}
		child.mParent = this; 
	}
	
	private String mTag;
	private Token mToken;
	private List<ASTNode> mChildren = new ArrayList<ASTNode>();
	private ASTNode mParent;
}
