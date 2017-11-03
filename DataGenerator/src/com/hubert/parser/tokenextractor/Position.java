package com.hubert.parser.tokenextractor;

public class Position {
    public Position(int lineNumber) {
        mLineNumber = lineNumber;
    }
    
    public int getLineNumber(){
        return mLineNumber;
    }
    
    private int mLineNumber = -1;
}
