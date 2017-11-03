package com.hubert.parser.tokenextractor.YiAn;

import com.hubert.parser.tokenextractor.*;

public class YiAnToken extends Token{
    
    public YiAnToken(YiAnTokenType type, Position sourcePosition) {
        this(type, "", sourcePosition);
    }

    public YiAnToken(YiAnTokenType type, String value, Position sourcePosition) {
        super(value, sourcePosition);
        mType = type;
    }

    public String getType() {
        return mType.name();
    }

    private YiAnTokenType mType;
}
