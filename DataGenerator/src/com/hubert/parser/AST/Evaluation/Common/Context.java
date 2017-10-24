package com.hubert.parser.AST.Evaluation.Common;

import java.util.*;

import com.hubert.dal.entity.YiAnEntity;
import com.hubert.parser.AST.YiAn.Evaluation.Scope;

public class Context {

    public Context() {
    }

    public Scope createScope() {
        Scope scope = null;
        if (!mScopes.isEmpty()) {
            Scope parent = mScopes.peek();
            scope = new Scope(this, parent);
        } else {
            scope = new Scope(this);
        }

        return mScopes.push(scope);
    }

    public Scope getActiveScope() {
        return mScopes.peek();
    }

    public void remove() {
        if (mScopes.isEmpty()) {
            System.out.println("error happens");
        }

        Scope scope = mScopes.pop();
        scope.destroy();
    }

    private Stack<Scope> mScopes = new Stack<Scope>();
}
