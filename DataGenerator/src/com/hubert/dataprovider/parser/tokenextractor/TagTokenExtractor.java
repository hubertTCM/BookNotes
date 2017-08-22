package com.hubert.dataprovider.parser.tokenextractor;

import java.util.*;

import com.hubert.dataprovider.parser.*;
import javafx.util.Pair;

public class TagTokenExtractor implements ITokenExtractor {
	public TagTokenExtractor(TokenType tokenType) {
		mTokenType = tokenType;
		mTags = new ArrayList<Pair<String, Boolean>>();
	}

	public void registerTag(String tag, boolean isRawTag) {
		mTags.add(new Pair<String, Boolean>(tag, isRawTag));
	}

	@Override
	public boolean extract(String text, Collection<Token> container) {
		// TODO Auto-generated method stub
		return extractCore(text, container);
	}

	protected boolean extractCore(String text, Collection<Token> container) {
		for (Pair<String, Boolean> temp : mTags) {
			if (!text.startsWith(temp.getKey())) {
				continue;
			}

			if (temp.getValue()) {
				container.add(new Token(mTokenType, text));
				return true;
			}

			String formattedText = text.substring(temp.getKey().length());
			container.add(new Token(mTokenType, formattedText));
			return true;
		}
		return false;
	}

	// isRawTag
	protected ArrayList<Pair<String, Boolean>> mTags;
	protected TokenType mTokenType;

}
