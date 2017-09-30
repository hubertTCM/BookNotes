package com.hubert.parser.AST.YiAn.Evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hubert.parser.AST.ASTNode;

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
        mScope = mContext.createScope();
        return evaluateCore(node);
    }

    @Override
    public boolean postEvaluate(ASTNode node) {
        mScope = mContext.getActiveScope();
        boolean success = postEvaluateCore(node);
        // RecipeCompositionHerbOnly is parent of self, leads the scope is
        // shared.
        // if (mShouldCreateScope) {
        // //mScope.destroy();
        // }
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
