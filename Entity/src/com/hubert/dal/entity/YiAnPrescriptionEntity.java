package com.hubert.dal.entity;

import java.util.*;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

// TODO: only save the summary for search
@DatabaseTable(tableName = "yianprescription")
public class YiAnPrescriptionEntity {
    @DatabaseField(generatedId = true)
    public long id;

    // 上午服，下午服
    @DatabaseField()
    public long order;

    // 煎服法
    @DatabaseField(width = 1500)
    public String comment;

    // 桂枝 白芍 甘草 生姜 大枣
    // cache the composition, easy to analyze
    @DatabaseField(width = 1500)
    public String summary;

    @DatabaseField(canBeNull = false, foreign = true)
    public YiAnDetailEntity yian;

    @ForeignCollectionField(eager = false)
    public Collection<YiAnPrescriptionItemEntity> items;

    @DatabaseField(canBeNull = false, foreign = true)
    public BlockEntity block;
}
