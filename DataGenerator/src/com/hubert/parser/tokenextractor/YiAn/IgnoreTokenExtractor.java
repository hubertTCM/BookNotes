package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;

import javafx.util.Pair;

public class IgnoreTokenExtractor implements ITokenExtractor {
    public IgnoreTokenExtractor() {
        mTags = new ArrayList<String>();
        mTags.add("// comment");
    }

    @Override
    public Pair<Boolean, String> extract(String text, List<Token> container) {
        for (String temp : mTags) {
            if (text.startsWith(temp)) {
                return new Pair<>(true, "");
            }
        }

        return null;
    }

    private ArrayList<String> mTags;

}
