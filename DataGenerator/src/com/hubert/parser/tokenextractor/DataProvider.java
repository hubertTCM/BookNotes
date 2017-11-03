package com.hubert.parser.tokenextractor;

import java.util.*;

public class DataProvider {

    public void setContent(int line, String content) {
        mData.put(line, content);
    }

    public String getContent(Position position) {
        int lineNumber = position.getLineNumber();
        if (mData.containsKey(lineNumber)) {
            return mData.get(lineNumber);
        }
        return null;
    }

    public void clear() {
        mData.clear();
    }

    private Map<Integer, String> mData = new HashMap<Integer, String>();
}
