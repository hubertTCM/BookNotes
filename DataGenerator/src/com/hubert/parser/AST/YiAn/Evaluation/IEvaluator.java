package com.hubert.parser.AST.YiAn.Evaluation;

import com.hubert.parser.AST.*;

public interface IEvaluator {
    public boolean canEvaluate(ASTNode node);
    public boolean evaluate(ASTNode node);
    public boolean postEvaluate(ASTNode node);
}
