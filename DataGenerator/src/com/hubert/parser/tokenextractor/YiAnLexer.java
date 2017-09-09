package com.hubert.parser.tokenextractor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import javafx.util.Pair;

public class YiAnLexer {
	public YiAnLexer(String fileFullPath) {
		mFullPath = fileFullPath;
		mTokenExtractors = new ArrayList<ITokenExtractor>();

		initTokenExtractors();
	}

	public List<Token> parse() throws IOException {
		mTokens = new ArrayList<Token>();

		Path filePath = Paths.get(mFullPath);
		Charset utf8 = Charset.forName("UTF-8");
		List<String> lines = Files.readAllLines(filePath, utf8);

		for (String temp : lines) {
			String line = StringUtils.trim(temp);
			if (line.isEmpty()){
				if (!mTokens.isEmpty()){
					mTokens.add(new Token(TokenType.End));
				}
				continue;
			}

			boolean isValid = false;
			for (ITokenExtractor extractor : mTokenExtractors) {
				Pair<Boolean, String> extractResult = extractor.extract(line, mTokens);
				if (extractResult == null) {
					continue;
				}
				isValid = extractResult.getKey();
				if (isValid) {
					break;
				}
			}

			if (!isValid) {
				//System.out.println(" **** Unknow Token: " + line);
				mTokens.add(new Token(TokenType.LiteralText, line));
				continue;
			}
		}

		return mTokens;
	}

	private void initTokenExtractors() {
		mTokenExtractors.add(new IgnoreTokenExtractor());
		mTokenExtractors.add(new YiAnDescriptionExtractor());
		
		TagTokenExtractor comment = new TagTokenExtractor(TokenType.SummaryComment);
		comment.registerTag("[comment]", false);
		comment.registerTag("徐评", true);
		mTokenExtractors.add(comment);

		TagTokenExtractor abbreviation = new TagTokenExtractor(TokenType.RecipeAbbreviation);
		abbreviation.registerTag("[abbr]");
		mTokenExtractors.add(abbreviation);

		TagTokenExtractor prescriptionHeader = new TagTokenExtractor(TokenType.RecipeHeader);
		prescriptionHeader.registerTag("[RH]");
		mTokenExtractors.add(prescriptionHeader);

		NewYiAnTagExtractor yiAnTagExtractor = new NewYiAnTagExtractor();
		mTokenExtractors.add(yiAnTagExtractor);

		PrescriptionItemTokenExtractor prescriptionItemToken = new PrescriptionItemTokenExtractor();
		FormattedPrescriptionExtractor formattedPrescriptionExtractor = 
				new FormattedPrescriptionExtractor(prescriptionItemToken);
		mTokenExtractors.add(formattedPrescriptionExtractor);	
		
		mTokenExtractors.add(new PrescriptionTokenExtractor(prescriptionItemToken));	
	}

	private String mFullPath;
	private ArrayList<Token> mTokens;
	private ArrayList<ITokenExtractor> mTokenExtractors;
}
