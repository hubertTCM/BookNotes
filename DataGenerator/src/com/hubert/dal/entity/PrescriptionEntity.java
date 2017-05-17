package com.hubert.dal.entity;

import com.j256.ormlite.field.DatabaseField;

// 处方
public class PrescriptionEntity {
    @DatabaseField(generatedId = true)
	public long id;

    @DatabaseField(width=100)
    public String name;
    
    // 煎服法
    @DatabaseField(width=1500)
    public String comment;

    // 桂枝 白芍 甘草 生姜 大枣
    // cache the composition, easy to analyze 
    @DatabaseField(width=1500)
    public String summary;
    
    @DatabaseField()
    public long order;

    @DatabaseField(canBeNull = true, foreign = true)
    public BlockEntity block;  
}
