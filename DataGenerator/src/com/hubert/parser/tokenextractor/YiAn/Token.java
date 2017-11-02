package com.hubert.parser.tokenextractor.YiAn;

public class Token {
    public Token(TokenType type) {
        this(type, "");
    }

    public Token(TokenType type, String value) {
        mType = type;
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    public String getType() {
        return mType.name();
    }

    private TokenType mType;
    private String mValue;
}
