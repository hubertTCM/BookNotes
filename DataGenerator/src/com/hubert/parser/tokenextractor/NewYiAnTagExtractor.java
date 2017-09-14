package com.hubert.parser.tokenextractor;

import java.util.*;

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
		// TODO: add check here
		if (mParentTokenType == TokenType.None){
			container.add(new Token(TokenType.Description, text));
			return new Pair<>(true, text);
		}
		if (mParentTokenType == TokenType.FormattedRecipeText){
			container.add(new Token(TokenType.Description, tag));
			return new Pair<>(true, text.substring(tag.length()));
		}
		System.out.println("** invalid token:" + text);
		return new Pair<>(false, "");
	}

	private TokenType mParentTokenType;

}
