package com.hubert.parser.tokenextractor;

import java.util.*;

import javafx.util.*;

public class DataProvider {
    public DataProvider(){
        mContentTypeTags.put(ContentType.OrignalText, new Pair<>("[", "]"));
        mContentTypeTags.put(ContentType.AdditionalText, new Pair<>("<", ">"));
    }
    
    public Pair<String, String> getContentTypeTag(ContentType contentType){
        return mContentTypeTags.get(contentType);
    }
    
    public void setContent(int line, String content) {
        InternalData data = getOrCreate(line);
        data.contentType = ContentType.OrignalText;
        data.content = content;
    }

    public void setContentType(int line, ContentType contentType) {
        InternalData data = getOrCreate(line);
        data.contentType = contentType;
    }

    public void setContentType(Position position, ContentType contentType) {
        int lineNumber = position.getLineNumber();
        setContentType(lineNumber, contentType);
    }

    public String getContent(Position position) {
        String content = "";
        int lineNumber = position.getLineNumber();
        if (mData.containsKey(lineNumber)) {
            InternalData data = mData.get(lineNumber);
            content = data.content;
            int index = content.indexOf(Constants.TokenEndTag);
            content = content.substring(index + 1);
        }
        return content;
    }

    public ContentType getContentType(Position position) {
        int lineNumber = position.getLineNumber();
        InternalData data = get(lineNumber);
        return data.contentType;
    }

    public void clear() {
        mData.clear();
    }

    private InternalData get(int lineNumber) {
        if (mData.containsKey(lineNumber)) {
            return mData.get(lineNumber);
        }
        return null;
    }

    private InternalData getOrCreate(int lineNumber) {
        if (!mData.containsKey(lineNumber)) {
            InternalData data = new InternalData();
            data.lineNumber = lineNumber;
            mData.put(lineNumber, data);
        }
        return mData.get(lineNumber);
    }

    private Map<Integer, InternalData> mData = new HashMap<Integer, InternalData>();

    private Map<ContentType, Pair<String, String>> mContentTypeTags = new HashMap<ContentType, Pair<String, String>>();
    
    private class InternalData {
        public int lineNumber;
        public ContentType contentType;
        public String content;
    }
}
