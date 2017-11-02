package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;

import javafx.util.Pair;

public class TagTokenExtractor implements ITokenExtractor {
    public TagTokenExtractor(TokenType tokenType) {
        mTokenType = tokenType;
        mTags = new ArrayList<Pair<String, Boolean>>();
    }

    public void registerTag(String tag, boolean isRawTag) {
        mTags.add(new Pair<String, Boolean>(tag, isRawTag));
    }

    public void registerTag(String tag) {
        boolean isRawTag = !tag.startsWith("[");
        registerTag(tag, isRawTag);
    }

    @Override
    public Pair<Boolean, String> extract(String text, List<Token> container) {
        // TODO Auto-generated method stub
        return extractCore(text, container);
    }

    protected Pair<Boolean, String> extractCore(String text, Collection<Token> container) {
        for (Pair<String, Boolean> temp : mTags) {
            if (!text.startsWith(temp.getKey())) {
                continue;
            }

            if (temp.getValue()) {
                container.add(new Token(mTokenType, text));
                return new Pair<>(true, text);
            }

            String formattedText = text.substring(temp.getKey().length());
            container.add(new Token(mTokenType, formattedText));
            return new Pair<>(true, formattedText);
        }
        return new Pair<>(false, "");
    }

    // isRawTag
    protected ArrayList<Pair<String, Boolean>> mTags;
    protected TokenType mTokenType;

}
