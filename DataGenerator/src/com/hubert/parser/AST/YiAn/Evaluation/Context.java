package com.hubert.parser.AST.YiAn.Evaluation;

import java.util.*;

import com.hubert.dal.entity.YiAnEntity;

public class Context {

    public Context(List<YiAnEntity> yiAn) {
        mYiAns = yiAn;
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

    public void remove(Scope scope) {
        if (mScopes.peek() != scope) {
            // TODO:
        }
        mScopes.pop();
    }

    public void remove() {
        if (mScopes.isEmpty()) {
            System.out.println("error happens");
        }

        mScopes.pop();
    }

    public void addYiAn(YiAnEntity yiAn) {
        mYiAns.add(yiAn);
    }

    private Stack<Scope> mScopes = new Stack<Scope>();
    private List<YiAnEntity> mYiAns; // = new ArrayList<YiAnEntity>();
}
