package com.hubert.parser.tokenextractor;

public abstract class Token {
    protected Token(String value) {
        this(value, null);
    }
    
    protected Token(String value, Position sourcePosition){
        mValue = value;
        mSourcePosition = sourcePosition;
    }

    public String getValue() {
        return mValue;
    }
    
    public Position getSourcePosition(){
        return mSourcePosition;
    }

    public abstract String getType();

    protected String mValue;
    protected Position mSourcePosition;
}
