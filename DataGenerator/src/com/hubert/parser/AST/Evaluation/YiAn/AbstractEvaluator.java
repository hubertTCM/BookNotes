package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.Context;

public abstract class AbstractEvaluator implements IEvaluator {

    protected AbstractEvaluator(String acceptableTag, Context context) {
        this(Arrays.asList(acceptableTag), context);
    }

    protected AbstractEvaluator(List<String> acceptableTags, Context context) {
        mContext = context;
        mAcceptableTags.addAll(acceptableTags);
    }

    @Override
    public boolean canEvaluate(ASTNode node) {
        if (mAcceptableTags.contains(node.getTag())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean evaluate(ASTNode node) {
        mYiAnScope = new YiAnScope(mContext.createStorage());
        return evaluateCore(node);
    }

    @Override
    public boolean postEvaluate(ASTNode node) {
        mYiAnScope = new YiAnScope(mContext.getActiveStorage());
        return postEvaluateCore(node);
    }

    @Override
    public boolean clear() {
        mYiAnScope = null;
        mContext.remove();
        return true;
    }

    protected boolean postEvaluateCore(ASTNode node) {
        return true;
    }

    protected abstract boolean evaluateCore(ASTNode node);

    protected Context mContext;

    protected YiAnScope mYiAnScope;

    protected List<String> mAcceptableTags = new ArrayList<String>();
}
