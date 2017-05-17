package com.hubert.dal.entity;

import com.j256.ormlite.field.DatabaseField;

// 医话
public class NoteEntity {
    @DatabaseField(generatedId = true)
	public long id;
    
    @DatabaseField(width=3000)
    public String content;
    
    @DatabaseField()
    public long order;

    @DatabaseField(canBeNull = true, foreign = true)
    public BlockEntity block;   
}
