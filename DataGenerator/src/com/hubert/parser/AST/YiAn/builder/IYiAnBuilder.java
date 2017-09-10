package com.hubert.parser.AST.YiAn.builder;

import com.hubert.parser.AST.*;

public interface IYiAnBuilder {
	public String getNodeTag();
	public boolean build(ASTNode node);
}
