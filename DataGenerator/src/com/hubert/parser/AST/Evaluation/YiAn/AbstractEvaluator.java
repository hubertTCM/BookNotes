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
        mScope = new Scope(mContext.createStorage());
        return evaluateCore(node);
    }

    @Override
    public boolean postEvaluate(ASTNode node) {
        mScope = new Scope(mContext.getActiveStorage());
        boolean success = postEvaluateCore(node);
        mScope = null;
        mContext.remove();
        return success;
    }

    protected boolean postEvaluateCore(ASTNode node) {
        return true;
    }

    protected abstract boolean evaluateCore(ASTNode node);

    protected Context mContext;

    protected Scope mScope;

    protected List<String> mAcceptableTags = new ArrayList<String>();
}
