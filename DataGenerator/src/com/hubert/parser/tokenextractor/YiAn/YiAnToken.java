package com.hubert.parser.tokenextractor.YiAn;

import com.hubert.parser.tokenextractor.Token;

public class YiAnToken extends Token{
    public YiAnToken(YiAnTokenType type) {
        this(type, "");
    }

    public YiAnToken(YiAnTokenType type, String value) {
        super(value);
        mType = type;
    }

    public String getType() {
        return mType.name();
    }

    private YiAnTokenType mType;
}
