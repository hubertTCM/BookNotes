package com.hubert.parser;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;

public final class Utils {
    // 1.卷一
    // 2.中风.txt
    public static String getSectionName(String fileName) {
        int index = fileName.indexOf(".");
        fileName = fileName.substring(index + 1).trim();

        index = fileName.indexOf(".");
        if (index > 0) {
            return fileName.substring(0, index);
        }
        return StringUtils.strip(fileName);
    }

    public static SectionEntity createSection(String sectionName) {
        SectionEntity entity = new SectionEntity();
        entity.name = sectionName; // getSectionName(fileName);
        entity.childSections = new ArrayList<SectionEntity>();
        entity.blocks = new ArrayList<BlockEntity>();
        return entity;
    }

    public static <K, V> List<V> merge(Map<K, List<V>> source) {
        List<V> result = new ArrayList<V>();
        for(Map.Entry<K, List<V>> entry : source.entrySet()){
            result.addAll(entry.getValue());
        }
        return result;
    }
}
