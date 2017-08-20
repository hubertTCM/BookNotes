package com.hubert.dal.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "yiandetailblocklink")
public class YiAnDetailBlockLinkEntity {
	@DatabaseField(generatedId = true)
	public long id;

	@DatabaseField(canBeNull = true, foreign = true)
	public BlockEntity block;

	@DatabaseField(canBeNull = true, foreign = true)
	public YiAnDetailEntity yian;
	
	// public String BlockType; // Description, Prescription, etc 
}
