package com.hubert.parser.tokenextractor.YiAn;

import java.util.*;

import com.hubert.parser.tokenextractor.*;
import org.apache.commons.lang3.StringUtils;

import javafx.util.Pair;

public class YiAnDescriptionExtractor implements ITokenExtractor {

    @Override
    public Pair<Boolean, String> extract(String text, Position sourcePosition, List<Token> container) {
        if (text.isEmpty()) {
            return null;
        }

        Token previousToken = null;
        if (!container.isEmpty()) {
            previousToken = container.get(container.size() - 1);
        }

        if (previousToken != null && !YiAnTokenType.End.name().equals(previousToken.getType())) {
            return null;
        }

        // 沈（四九） 脉细而数，细为脏阴之亏，数为营液之耗。上年夏秋病伤，更因冬暖失藏....
        if (!text.endsWith("）")) {
            container.add(new YiAnToken(YiAnTokenType.Description, text, sourcePosition));
            return new Pair<>(true, "");
        }
        // 阳挟内风上巅，目昏耳鸣不寐，肝经主病。
        String delimiter = "（";
        if (!text.contains(delimiter)) {
            return new Pair<>(true, text);
        }

        // 汪（五三） 左肢麻木，膝盖中牵纵，忽如针刺。中年后，精血内虚，虚风自动，乃阴中之阳损伤。（阴中阳虚。）
        int index = text.lastIndexOf(delimiter);
        if (index <= 0) {
            System.out.println(" ****" + text);
            return null;
        }
        String descripton = StringUtils.trim(text.substring(0, index));
        String sectionName = StringUtils.trim(text.substring(index + 1));
        if (sectionName.contains("。")) {
            String[] parts = sectionName.split("。");
            if (parts.length != 2) {
                System.out.println(" ****" + text);
                return null;
            }
            sectionName = StringUtils.trim(parts[0]);
        } else {
            // 某 内风，乃身中阳气之动变，甘酸之属宜之。（肝阴虚）
            sectionName = sectionName.substring(0, sectionName.length() - 1);
        }

        container.add(new YiAnToken(YiAnTokenType.SectionName, sectionName, sourcePosition));
        container.add(new YiAnToken(YiAnTokenType.Description, descripton, sourcePosition));

        return new Pair<>(true, text);
    }

}
