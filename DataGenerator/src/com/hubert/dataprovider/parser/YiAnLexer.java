package com.hubert.dataprovider.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.*;

public class YiAnLexer {
	public YiAnLexer(String fileFullPath) {
		mFullPath = fileFullPath;
	}

	public Collection<Token> parse() throws IOException {
		ArrayList<Token> tokens = new ArrayList<Token>();

		Path filePath = Paths.get(mFullPath);
		Charset utf8 = Charset.forName("UTF-8");
		List<String> lines = Files.readAllLines(filePath, utf8);

		Token previousToken = null;
		for (String temp : lines) {
			String line = StringUtils.trim(temp);
			if (line.startsWith("// comment")) {
				continue;
			}
			if (line.isEmpty()) {
				Token token = new Token(TokenType.BlankSpace, line);
				tokens.add(token);

				previousToken = null;
				continue;
			}
			
			if (line.startsWith("[comment]") || line.startsWith("徐评")){
				tokens.add(new Token(TokenType.SummaryComment, line));
				continue;
			}

			if (previousToken == null) {
				ImmutablePair<String, String> pair = splitYiAnDescription(line);
				String sectionName = pair.getRight();
				if (!sectionName.isEmpty()) {
					tokens.add(new Token(TokenType.SectionName, sectionName));
				}

				Token token = new Token(TokenType.YiAnDescription, pair.getLeft());
				tokens.add(token);
				
				previousToken = token;
				continue;
			}

			previousToken = tokens.get(tokens.size() - 1);

			if (line.startsWith("[RH]")) {
				tokens.add(new Token(TokenType.PrescriptionHeader, line));
				continue;
			}

			if (line.startsWith("[abbr]")) {
				tokens.add(new Token(TokenType.PrescriptionAbbreviation, line));
				continue;
			}

			if (line.startsWith("[format]")) {
				ImmutablePair<String, Boolean> pair = splitPrescriptionDescription(line);
				if (pair.getRight()) {
					tokens.add(new Token(TokenType.YiAnDescription, ""));
				}
				tokens.add(new Token(TokenType.PrescriptionFormatted, pair.getLeft()));
				continue;
			}
			// （丸方） 人参（二两） 茯苓（三两，生） 盐水炒黄连（五钱） 半夏（醋炒，水洗净，一两半） ....
			String tempTag = "（丸方）";
			if (line.startsWith(tempTag)) {
				tokens.add(new Token(TokenType.PrescriptionFormatted, line.substring(tempTag.length())));
				continue;
			}

			if (line.startsWith("又")) {
				tokens.add(new Token(TokenType.YiAnDescription, line));
				continue;
			}

			if (previousToken.getType() == TokenType.YiAnDescription) {
				tokens.add(new Token(TokenType.PrescriptionFormatted, line));
				continue;
			}

			if (previousToken.getType() == TokenType.PrescriptionFormatted) {
				tokens.add(new Token(TokenType.PrescriptionComment, line));
				continue;
			}

			// #error:
			System.out.println(" **** Unknow Token: " + line);
		}

		return tokens;
	}

	// 钱 偏枯在左，血虚不萦筋骨，内风袭络，脉左缓大。（肝肾虚内风动。）
	// https://stackoverflow.com/questions/3481828/how-to-split-a-string-in-java
	private static ImmutablePair<String, String> splitYiAnDescription(String source) {
		String delimiter = "（";
		if (!source.contains(delimiter)) {
			return new ImmutablePair<String, String>(source, "");
		}
		String[] parts = source.split(delimiter);
		if (parts.length != 2) {
			return null;
		}

		String text = parts[0];
		String sectionName = StringUtils.trim(parts[1]);
		parts = sectionName.split("。");
		if (parts.length != 2) {
			return null;
		}
		sectionName = StringUtils.trim(parts[0]);
		ImmutablePair<String, String> pair = new ImmutablePair<String, String>(text, sectionName);
		return pair;
	}

	private ImmutablePair<String, Boolean> splitPrescriptionDescription(String source) {
		String tag = "[format]";
		if (!source.startsWith(tag)) {
			return null;
		}
		String text = StringUtils.trim(source.substring(tag.length()));
		if (text.startsWith("又")) {
			return new ImmutablePair<String, Boolean>(text.substring(1), true);
		}
		return new ImmutablePair<String, Boolean>(text, false);
	}

	private String mFullPath;
}
