package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;

import com.hubert.parser.tokenextractor.*;

import javafx.util.Pair;

public class RecipeContentExtractor implements ITokenExtractor {
    public RecipeContentExtractor(ITokenExtractor prescriptionItemExtractor, DataProvider dataProvider) {
        mPrescriptionItemExtractor = prescriptionItemExtractor;
        mDataProvider = dataProvider;

        for (ContentType contentType : ContentType.values()) {
            Pair<String, String> temp = mDataProvider.getContentTypeTag(contentType);
            String fullTagText = temp.getKey() + "format" + temp.getValue();
            mTags.add(new Pair<>(fullTagText, contentType));
        }
    }

    @Override
    public Pair<Boolean, String> extract(String text, Position sourcePosition, List<Token> container) {
        for (Pair<String, ContentType> entry : mTags) {
            String tag = entry.getKey();
            if (!text.startsWith(tag)) {
                continue;
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

        return null;
    }

    // （丸方）
    // [format]
    private ITokenExtractor mPrescriptionItemExtractor;
    private DataProvider mDataProvider;
    private ArrayList<Pair<String, ContentType>> mTags = new ArrayList<Pair<String, ContentType>>();
}
