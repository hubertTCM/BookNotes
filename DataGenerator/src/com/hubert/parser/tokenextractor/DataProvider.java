package com.hubert.parser.tokenextractor;

import java.util.*;

import javafx.util.*;

public class DataProvider {
    public DataProvider() {
        mContentTypeTags.put(ContentType.OrignalText, new Pair<>("[", "]"));
        mContentTypeTags.put(ContentType.AdditionalText, new Pair<>("<", ">"));
    }

    public Pair<String, String> getContentTypeTag(ContentType contentType) {
        return mContentTypeTags.get(contentType);
    }

    public void setContent(int line, String content) {
        InternalData data = getOrCreate(line);
        data.contentType = ContentType.OrignalText;
        data.content = content;

        if (content == null) {
            return;
        }

        for (Map.Entry<ContentType, Pair<String, String>> temp : mContentTypeTags.entrySet()) {
            if (content.startsWith(getContentTypeStartTag(temp.getKey()))) {
                data.contentType = temp.getKey();
                break;
            }
        }

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
            String endTag = getContentTypeEndTag(data.contentType);
            int index = content.indexOf(endTag);
            content = content.substring(index + 1);
        }
        return content;
    }

    public String getFullContent(Position position, String tokenType) {
        String content = "";
        int lineNumber = position.getLineNumber();
        if (mData.containsKey(lineNumber)) {
            InternalData data = mData.get(lineNumber);
            content = getContent(position);
            String startTag = getContentTypeStartTag(data.contentType);
            String endTag = getContentTypeEndTag(data.contentType);
            content = startTag + tokenType + endTag + content;
        }
        return content;

    }

    public String getEndLine() {

        ContentType contentType = ContentType.OrignalText;
        String startTag = getContentTypeStartTag(contentType);
        String endTag = getContentTypeEndTag(contentType);

        return startTag + Constants.End + endTag;
    }

    public ContentType getContentType(Position position) {
        int lineNumber = position.getLineNumber();
        InternalData data = get(lineNumber);
        return data.contentType;
    }

    public void clear() {
        mData.clear();
    }

    private String getContentTypeStartTag(ContentType contentType) {
        return mContentTypeTags.get(contentType).getKey();
    }

    private String getContentTypeEndTag(ContentType contentType) {
        return mContentTypeTags.get(contentType).getValue();
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
