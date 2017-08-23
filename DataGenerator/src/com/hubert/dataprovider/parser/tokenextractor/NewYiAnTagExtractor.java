package com.hubert.dataprovider.parser.tokenextractor;

import java.util.*;

import com.hubert.dataprovider.parser.*;

import javafx.util.Pair;

public class NewYiAnTagExtractor implements ITokenExtractor {
	public NewYiAnTagExtractor() {
		mParentTokenType = TokenType.None;
	}
	
	public NewYiAnTagExtractor(TokenType parentTokenType){
		mParentTokenType = parentTokenType;
	}

	@Override
	public Pair<Boolean, String> extract(String text, List<Token> container) {
		String tag = "又";
		if (!text.startsWith(tag)) {
			return new Pair<>(false, "");
		}
		if (mParentTokenType == TokenType.None){
			container.add(new Token(TokenType.YiAnDescription, text));
			return new Pair<>(true, text);
		}
		if (mParentTokenType == TokenType.PrescriptionFormatted){
			container.add(new Token(TokenType.YiAnDescription, tag));
			return new Pair<>(true, text.substring(tag.length()));
		}
		System.out.println("** invalid token:" + text);
		return new Pair<>(false, "");
	}

	private TokenType mParentTokenType;

}
