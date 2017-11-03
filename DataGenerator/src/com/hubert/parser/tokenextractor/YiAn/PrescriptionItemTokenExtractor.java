package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;
import com.hubert.parser.tokenextractor.*;

import org.apache.commons.lang3.StringUtils;

import javafx.util.Pair;

public class PrescriptionItemTokenExtractor implements ITokenExtractor {
    @Override
    public Pair<Boolean, String> extract(String text, Position sourcePosition, List<Token> container) {
        // 熟地（五钱） 咸苁蓉（八钱） 炒杞子（三钱） 麦冬（二钱） 云苓（一钱半）
        String[] compositions = text.split("\\s+");
        for (String part : compositions) {
            extractSinglePrescriptionItem(part, sourcePosition, container);
        }
        return null;
    }

    // 熟地（五钱）
    private void extractSinglePrescriptionItem(String text, Position sourcePosition, List<Token> container) {
        text = StringUtils.strip(text);
        if (text.isEmpty()) {
            return;
        }

        int index = text.indexOf("（");
        if (index < 0) {
            index = text.indexOf("(");
        }

        String herb;
        if (index < 0) {
            herb = text;
        } else {
            herb = text.substring(0, index);
        }
        herb = StringUtils.strip(herb);
        if (herb.isEmpty()) {
            return;
        }
        container.add(new YiAnToken(YiAnTokenType.Herb, herb, sourcePosition));
    }

}
