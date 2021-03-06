package com.hubert.parser.tokenextractor;

import java.util.*;

import javafx.util.Pair;

public interface ITokenExtractor {
    public Pair<Boolean, String> extract(String text, Position sourcePosition, List<Token> container);
}
