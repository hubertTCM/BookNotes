package com.hubert.dal.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

// 处方
@DatabaseTable(tableName = "prescription")
public class PrescriptionEntity {
    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(width = 100)
    public String name;

    // 煎服法
    @DatabaseField(width = 1500)
    public String comment;

    // 桂枝 白芍 甘草 生姜 大枣
    // cache the composition, easy to analyze
    @DatabaseField(width = 1500)
    public String summary;
}
