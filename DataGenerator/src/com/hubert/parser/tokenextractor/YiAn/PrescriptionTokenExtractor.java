package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;
import com.hubert.parser.tokenextractor.*;

import javafx.util.Pair;

public class PrescriptionTokenExtractor implements ITokenExtractor {
    public PrescriptionTokenExtractor(ITokenExtractor prescriptionItemExtractor) {
        mPrescriptionItemExtractor = prescriptionItemExtractor;
    }

    @Override
    public Pair<Boolean, String> extract(String text, Position sourcePosition, List<Token> container) {
        // （丸方） 人参（二两） 茯苓（三两，生） 盐水炒黄连（五钱） 半夏（醋炒，水洗净，一两半） ....
        String tag = "（丸方）";
        if (text.startsWith(tag)) {
            String source = text.substring(tag.length());
            // container.add(new Token(TokenType.FormattedRecipeText, source));
            mPrescriptionItemExtractor.extract(source, sourcePosition, container);
            return new Pair<>(true, "");
        }
        return null;
    }

    private ITokenExtractor mPrescriptionItemExtractor;
}
