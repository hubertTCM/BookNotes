package com.hubert.parser.tokenextractor.YiAn;

import java.io.IOException;
import com.hubert.parser.tokenextractor.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

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

        //for (String temp : lines) {
        for(int i = 0; i < lines.size(); ++i){
            String temp = lines.get(i);
            Position position = new Position(i);
            String line = StringUtils.trim(temp);
            if (line.isEmpty()) {
                if (!mTokens.isEmpty()) {
                    mTokens.add(new YiAnToken(YiAnTokenType.End, position));
                }
                continue;
            }

            boolean isValid = false;
            for (ITokenExtractor extractor : mTokenExtractors) {
                Pair<Boolean, String> extractResult = extractor.extract(line, position, mTokens);
                if (extractResult == null) {
                    continue;
                }
                isValid = extractResult.getKey();
                if (isValid) {
                    break;
                }
            }

            if (!isValid) {
                // System.out.println(" **** Unknow Token: " + line);
                mTokens.add(new YiAnToken(YiAnTokenType.LiteralText, line, position));
                continue;
            }
        }

        return mTokens;
    }

    private void initTokenExtractors() {
        mTokenExtractors.add(new IgnoreTokenExtractor());
        mTokenExtractors.add(new YiAnDescriptionExtractor());

        TagTokenExtractor comment = new TagTokenExtractor(YiAnTokenType.SummaryComment);
        comment.registerTag("[comment]", false);
        comment.registerTag("徐评", true);
        mTokenExtractors.add(comment);

        TagTokenExtractor abbreviation = new TagTokenExtractor(YiAnTokenType.RecipeAbbreviation);
        abbreviation.registerTag("[abbr]");
        mTokenExtractors.add(abbreviation);

        TagTokenExtractor prescriptionHeader = new TagTokenExtractor(YiAnTokenType.RecipeHeader);
        prescriptionHeader.registerTag("[RH]");
        mTokenExtractors.add(prescriptionHeader);

        NewYiAnTagExtractor yiAnTagExtractor = new NewYiAnTagExtractor();
        mTokenExtractors.add(yiAnTagExtractor);

        PrescriptionItemTokenExtractor prescriptionItemToken = new PrescriptionItemTokenExtractor();
        FormattedPrescriptionExtractor formattedPrescriptionExtractor = new FormattedPrescriptionExtractor(
                prescriptionItemToken);
        mTokenExtractors.add(formattedPrescriptionExtractor);

        mTokenExtractors.add(new PrescriptionTokenExtractor(prescriptionItemToken));
    }

    private String mFullPath;
    private ArrayList<Token> mTokens;
    private ArrayList<ITokenExtractor> mTokenExtractors;
}
