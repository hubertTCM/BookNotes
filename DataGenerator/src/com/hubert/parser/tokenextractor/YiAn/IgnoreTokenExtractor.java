package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;

import com.hubert.parser.tokenextractor.*;

import javafx.util.Pair;

public class IgnoreTokenExtractor implements ITokenExtractor {
    public IgnoreTokenExtractor() {
        mTags = new ArrayList<String>();
        mTags.add("// comment");
        mTags.add("//");
    }

    @Override
    public Pair<Boolean, String> extract(String text, Position sourcePosition, List<Token> container) {
        for (String temp : mTags) {
            if (text.startsWith(temp)) {
                return new Pair<>(true, "");
            }
        }

        return null;
    }

    private ArrayList<String> mTags;

}
