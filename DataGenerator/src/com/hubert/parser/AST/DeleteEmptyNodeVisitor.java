package com.hubert.parser.AST;

public class DeleteEmptyNodeVisitor implements IVisitor {

	@Override
	public void visit(ASTNode node) {

		for(int i = 0; i < node.childCount(); ++i){
			node.getChild(i).accept(this);
		}
		
		if (node.isEmpty()){
			node.remove();
		}
	}

}
