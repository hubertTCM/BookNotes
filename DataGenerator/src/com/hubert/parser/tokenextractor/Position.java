package com.hubert.parser.tokenextractor;

public class Position implements Comparable<Position> {
    public Position(int lineNumber) {
        mLineNumber = lineNumber;
    }
    
    public int getLineNumber(){
        return mLineNumber;
    }
    
    private int mLineNumber = -1;

    @Override
    public int compareTo(Position o) {
        if (mLineNumber < o.mLineNumber){
            return -1;
        }
        if (mLineNumber > o.mLineNumber){
            return 1;
        }
        return 0;
    }
}
