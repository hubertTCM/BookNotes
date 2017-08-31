package com.hubert.dataprovider.parser.tokenextractor;

import java.util.*;

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

		Token previousToken = container.get(container.size() - 1);
		if (!PrescriptionTokenExtractor.isHerb(previousToken)
				&& previousToken.getType() != TokenType.RecipeComment) {

			System.out.println(
					" **** Warning format incorrect? no prescription found in previous Yi An. previous token type: "
							+ previousToken.getType() + " previous token value: " + previousToken.getValue());
		}

		container.add(new Token(TokenType.BlankSpace, text));
		return new Pair<>(true, text);
	}

}
