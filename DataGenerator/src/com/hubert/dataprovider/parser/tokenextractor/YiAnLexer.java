package com.hubert.dataprovider.parser.tokenextractor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dataprovider.parser.Token;
import com.hubert.dataprovider.parser.TokenType;

public class YiAnLexer {

	public YiAnLexer(String fileFullPath) {
		mFullPath = fileFullPath;
		mTokenExtractors = new ArrayList<ITokenExtractor>();
		
		initTokenExtractors();
	}

	public Collection<Token> parse() throws IOException {
		mTokens = new ArrayList<Token>();

		Path filePath = Paths.get(mFullPath);
		Charset utf8 = Charset.forName("UTF-8");
		List<String> lines = Files.readAllLines(filePath, utf8);

		for (String temp : lines) {
			String line  = StringUtils.trim(temp);
			
			boolean isValid = false;
			for(ITokenExtractor extractor : mTokenExtractors){
				isValid = extractor.extract(line, mTokens);
				if (isValid){
					break;
				}
			}
			
			if (!isValid){
				System.out.println(" **** Unknow Token: " + line);
				continue;
			}
		}

		return mTokens;
	}
	
	private void initTokenExtractors(){
		TagTokenExtractor comment = new TagTokenExtractor(TokenType.SummaryComment);
	}

	private String mFullPath;
	private ArrayList<Token> mTokens;
	private ArrayList<ITokenExtractor> mTokenExtractors;
}
