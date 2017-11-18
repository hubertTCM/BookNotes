package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;
import com.hubert.parser.tokenextractor.*;

import javafx.util.Pair;

public class TagTokenExtractor implements ITokenExtractor {
    public TagTokenExtractor(YiAnTokenType tokenType, DataProvider provider) {
        mTokenType = tokenType;
        mDataProvider = provider;
        registerTag(tokenType.name());
    }

    public void registerTag(String tag) {
        for (ContentType contentType : ContentType.values()) {
            Pair<String, String> temp = mDataProvider.getContentTypeTag(contentType);
            String fullTagText = temp.getKey() + tag + temp.getValue();
            mTags.add(new Pair<>(fullTagText, contentType));
        }
    }

    @Override
    public Pair<Boolean, String> extract(String text, Position sourcePosition, List<Token> container) {
        // TODO Auto-generated method stub
        return extractCore(text, sourcePosition, container);
    }

    protected Pair<Boolean, String> extractCore(String text, Position sourcePosition, Collection<Token> container) {
        for (Pair<String, ContentType> temp : mTags) {
            if (!text.startsWith(temp.getKey())) {
                continue;
            }

            String formattedText = text.substring(temp.getKey().length());
            container.add(new YiAnToken(mTokenType, formattedText, sourcePosition));
            mDataProvider.setContentType(sourcePosition, temp.getValue());
            return new Pair<>(true, formattedText);
        }
        return new Pair<>(false, "");
    }

    // isRawTag
    protected ArrayList<Pair<String, ContentType>> mTags = new ArrayList<Pair<String, ContentType>>();
    protected YiAnTokenType mTokenType;
    protected DataProvider mDataProvider;

}
