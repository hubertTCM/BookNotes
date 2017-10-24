package com.hubert.parser.AST.Evaluation.Common;

import java.util.HashMap;
import java.util.Map;

public class Storage {

    public Storage(Storage parent) {
        mParent = parent;
    }

    public Storage() {
        this(null);
    }

    public void destroy() {
        mParent = null;
        mVariables.clear();
        mVariables = null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getVariable(String key) {
        if (mVariables.containsKey(key)) {
            return (T) mVariables.get(key);
        }

        if (mParent != null) {
            return mParent.getVariable(key);
        }
        System.out.println("*** failed to find " + key);
        return null;
    }

    public <T> T setVariable(String key, T value) {
        if (mVariables.containsKey(key)) {
            // TODO:
        }
        mVariables.put(key, value);
        return value;
    }

    private Map<String, Object> mVariables = new HashMap<String, Object>();
    private Storage mParent;
}
