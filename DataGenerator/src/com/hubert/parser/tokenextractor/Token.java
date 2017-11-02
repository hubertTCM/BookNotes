package com.hubert.parser.tokenextractor;

public abstract class Token {
    protected Token(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    public abstract String getType();

    protected String mValue;
}
