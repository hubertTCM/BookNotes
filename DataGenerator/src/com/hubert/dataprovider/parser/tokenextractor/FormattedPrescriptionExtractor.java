package com.hubert.dataprovider.parser.tokenextractor;

import java.util.*;

import com.hubert.dataprovider.parser.Token;
import com.hubert.dataprovider.parser.TokenType;

import javafx.util.Pair;

public class FormattedPrescriptionExtractor implements ITokenExtractor {
	
	@Override
	public Pair<Boolean, String> extract(String text, List<Token> container) {
		if (!text.startsWith(tag)) {
			return null;
		}
		String source = text.substring(tag.length());
		NewYiAnTagExtractor extractor = new NewYiAnTagExtractor(TokenType.PrescriptionFormatted);
		Pair<Boolean, String> result = extractor.extract(source, container);
		if (result != null && result.getKey()) {
			source = result.getValue();
		}
		container.add(new Token(TokenType.PrescriptionFormatted, source));
		return new Pair<>(true, "");
	}

	// （丸方）
	// [format]
	private static final String tag= "[format]";
}
