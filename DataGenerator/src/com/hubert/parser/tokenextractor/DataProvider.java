package com.hubert.parser.tokenextractor;

import java.util.*;

public class DataProvider {

    public void setContent(int line, String content) {
        mData.put(line, content);
    }

    public String getContent(Position position) {
        String content = "";
        int lineNumber = position.getLineNumber();
        if (mData.containsKey(lineNumber)) {
            content = mData.get(lineNumber);
            int index = content.indexOf(Constants.TokenEndTag);
            content = content.substring(index + 1);
        }
        return content;
    }

    public void clear() {
        mData.clear();
    }

    private Map<Integer, String> mData = new HashMap<Integer, String>();
}
