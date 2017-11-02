package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;
import com.hubert.parser.tokenextractor.*;

import javafx.util.Pair;

public class NewYiAnTagExtractor implements ITokenExtractor {
    public NewYiAnTagExtractor() {
        mParentTokenType = TokenType.None;
    }

    public NewYiAnTagExtractor(TokenType parentTokenType) {
        mParentTokenType = parentTokenType;
    }

    @Override
    public Pair<Boolean, String> extract(String text, List<Token> container) {
        String tag = "又";
        if (!text.startsWith(tag)) {
            return new Pair<>(false, "");
        }
        // TODO: add check here
        if (mParentTokenType == TokenType.None) {
            container.add(new YiAnToken(TokenType.Description, text));
            return new Pair<>(true, text);
        }
        if (mParentTokenType == TokenType.FormattedRecipeText) {
            container.add(new YiAnToken(TokenType.Description, tag));
            return new Pair<>(true, text.substring(tag.length()));
        }
        System.out.println("** invalid token:" + text);
        return new Pair<>(false, "");
    }

    private TokenType mParentTokenType;

}
