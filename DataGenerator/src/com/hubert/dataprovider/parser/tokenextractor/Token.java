package com.hubert.dataprovider.parser.tokenextractor;

public class Token {
	public Token(TokenType type, String value) {
		mType = type;
		mValue = value;
	}

	public String getValue() {
		return mValue;
	}

	public TokenType getType() {
		return mType;
	}

	private TokenType mType;
	private String mValue;
}
