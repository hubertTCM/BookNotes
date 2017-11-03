package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;

import com.hubert.parser.tokenextractor.*;

import javafx.util.Pair;

public class FormattedPrescriptionExtractor implements ITokenExtractor {
    public FormattedPrescriptionExtractor(ITokenExtractor prescriptionItemExtractor) {
        mPrescriptionItemExtractor = prescriptionItemExtractor;
    }

    @Override
    public Pair<Boolean, String> extract(String text, Position sourcePosition, List<Token> container) {
        if (!text.startsWith(tag)) {
            return null;
        }
        String source = text.substring(tag.length());
        NewYiAnTagExtractor extractor = new NewYiAnTagExtractor(YiAnTokenType.FormattedRecipeText);
        Pair<Boolean, String> result = extractor.extract(source, sourcePosition, container);
        if (result != null && result.getKey()) {
            source = result.getValue();
        }
        mPrescriptionItemExtractor.extract(source, sourcePosition, container);
        return new Pair<>(true, "");
    }

    // （丸方）
    // [format]
    private static final String tag = "[format]";
    private ITokenExtractor mPrescriptionItemExtractor;
}
