package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

public class EntityProvider<T> {
    public EntityProvider(T data) {
        mData = data;
    }

    public T get() {
        return mData;
    }

    public boolean addOriginalSource(int lineNumber, String source) {
        if (!mOriginalSource.containsKey(lineNumber)) {
            mOriginalSource.put(lineNumber, source);
            return true;
        }

        String existingContent = mOriginalSource.get(lineNumber);
        return existingContent.equals(source);
    }

    private T mData;
    private Map<Integer, String> mOriginalSource = new HashMap<Integer, String>();
}
