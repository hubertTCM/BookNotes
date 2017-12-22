package com.hubert.dal.entity;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

@DatabaseTable(tableName = "blockreference")
public class BookReferenceEntity {
    @DatabaseField(generatedId = true)
    public long id;

    // 桂枝 白芍 甘草 生姜 大枣
    // cache the composition, easy to analyze
    @DatabaseField(width = 1500)
    public String summary;

    //public Collection<PrescriptionItem> items;

    @DatabaseField(canBeNull = false, foreign = true)
    public BlockEntity block;
}
