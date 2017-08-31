package com.hubert.dataprovider.parser.tokenextractor;

import java.util.*;

import javafx.util.Pair;

public class FormattedPrescriptionExtractor implements ITokenExtractor {
	public FormattedPrescriptionExtractor(ITokenExtractor prescriptionItemExtractor){
		mPrescriptionItemExtractor = prescriptionItemExtractor;
	}
	
	@Override
	public Pair<Boolean, String> extract(String text, List<Token> container) {
		if (!text.startsWith(tag)) {
			return null;
		}
		String source = text.substring(tag.length());
		NewYiAnTagExtractor extractor = new NewYiAnTagExtractor(TokenType.FormattedRecipeText);
		Pair<Boolean, String> result = extractor.extract(source, container);
		if (result != null && result.getKey()) {
			source = result.getValue();
		}
		container.add(new Token(TokenType.FormattedRecipeText, source));
		mPrescriptionItemExtractor.extract(source, container);
		return new Pair<>(true, "");
	}

	// （丸方）
	// [format]
	private static final String tag= "[format]";
	private ITokenExtractor mPrescriptionItemExtractor;
}
