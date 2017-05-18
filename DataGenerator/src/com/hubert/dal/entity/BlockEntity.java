package com.hubert.dal.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "block")
public class BlockEntity {
    @DatabaseField(generatedId = true)
	public long id;

    @DatabaseField(canBeNull = true, foreign = true)
    public SectionEntity section;   
    
    @DatabaseField(width=3000)
    public String content;
    
    @DatabaseField()
    public long order;
}
