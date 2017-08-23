package com.hubert.dataprovider.parser.tokenextractor;

import java.util.*;

import com.hubert.dataprovider.parser.Token;
import com.hubert.dataprovider.parser.TokenType;

import javafx.util.Pair;

public class BlankLineTokenExtractor implements ITokenExtractor {

	@Override
	public Pair<Boolean, String> extract(String text, List<Token> container) {
		if (!text.isEmpty()) {
			return null;
		}

		if (container.isEmpty()) {
			return new Pair<>(true, text);
		}

		// add check here later
		// Token previousToken = container.get(container.size() - 1);

		container.add(new Token(TokenType.BlankSpace, text));
		return new Pair<>(true, text);
	}

}
