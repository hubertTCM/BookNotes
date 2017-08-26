package com.hubert.dataprovider.parser.tokenextractor;

import java.util.List;

import javafx.util.Pair;

public class PrescriptionTokenExtractor implements ITokenExtractor {
	public PrescriptionTokenExtractor(ITokenExtractor prescriptionItemExtractor) {
		mPrescriptionItemExtractor = prescriptionItemExtractor;
	}

	@Override
	public Pair<Boolean, String> extract(String text, List<Token> container) {
		Token previousToken = null;
		if (!container.isEmpty()) {
			previousToken = container.get(container.size() - 1);
		}
		if (previousToken == null || previousToken.getType() == TokenType.BlankSpace) {
			return null;
		}

		// （丸方） 人参（二两） 茯苓（三两，生） 盐水炒黄连（五钱） 半夏（醋炒，水洗净，一两半） ....
		String tag = "（丸方）";
		if (text.startsWith(tag)) {
			String source = text.substring(tag.length());
			container.add(new Token(TokenType.PrescriptionFormatted, source));
			mPrescriptionItemExtractor.extract(source, container);
			return new Pair<>(true, "");
		}

		// 人参 茯苓 白蒺藜 炒半夏 炒杞子 甘菊
		if (previousToken.getType() == TokenType.YiAnDescription
				// || previousToken.getType() == TokenType.NewYiAnDescription
				|| previousToken.getType() == TokenType.PrescriptionHeader) {
			container.add(new Token(TokenType.PrescriptionFormatted, text));
			mPrescriptionItemExtractor.extract(text, container);
			return new Pair<>(true, "");
		}

		// 上午服。
		// if (previousToken.getType() == TokenType.PrescriptionFormatted) {
		if (isHerb(previousToken)) {
			container.add(new Token(TokenType.PrescriptionComment, text));
			return new Pair<>(true, "");
		}
		return null;
	}

	public static boolean isHerb(Token token) {
		TokenType tokenType = token.getType();
		if (tokenType == TokenType.Herb || tokenType == TokenType.HerbQuantity || tokenType == TokenType.HerbComment) {
			return true;
		}
		return false;
	}

	private ITokenExtractor mPrescriptionItemExtractor;
}
