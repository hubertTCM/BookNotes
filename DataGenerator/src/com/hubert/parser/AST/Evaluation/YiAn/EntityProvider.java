package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

public class EntityProvider<T> {
    public EntityProvider(T data) {
        mData = data;
    }

    public T get() {
        return mData;
    }

    public boolean addLine(int lineNumber, String source) {
        if (!mLines.containsKey(lineNumber)) {
            mLines.put(lineNumber, source);
            return true;
        }

        String existingContent = mLines.get(lineNumber);
        return existingContent.equals(source);
    }

    private T mData;
    private Map<Integer, String> mLines = new TreeMap<Integer, String>();
}
