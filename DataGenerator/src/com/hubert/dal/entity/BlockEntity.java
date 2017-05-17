package com.hubert.dal.entity;

import com.j256.ormlite.field.DatabaseField;

public class BlockEntity {
    @DatabaseField(generatedId = true)
	public long id;

    @DatabaseField(canBeNull = true, foreign = true)
    public SectionEntity section;   
    
    @DatabaseField(width=256)
    public String type;
    
    @DatabaseField()
    public long order;
}
