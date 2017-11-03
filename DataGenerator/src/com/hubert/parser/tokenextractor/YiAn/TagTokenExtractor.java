package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;
import com.hubert.parser.tokenextractor.*;

import javafx.util.Pair;

public class TagTokenExtractor implements ITokenExtractor {
    public TagTokenExtractor(YiAnTokenType tokenType) {
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
                container.add(new YiAnToken(mTokenType, text));
                return new Pair<>(true, text);
            }

            String formattedText = text.substring(temp.getKey().length());
            container.add(new YiAnToken(mTokenType, formattedText));
            return new Pair<>(true, formattedText);
        }
        return new Pair<>(false, "");
    }

    // isRawTag
    protected ArrayList<Pair<String, Boolean>> mTags;
    protected YiAnTokenType mTokenType;

}
