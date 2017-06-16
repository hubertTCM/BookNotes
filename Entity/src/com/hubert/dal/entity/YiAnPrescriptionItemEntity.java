package com.hubert.dal.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "yianprescriptionentity")
public class YiAnPrescriptionItemEntity {
	@DatabaseField(generatedId = true)
	public long id;

	@DatabaseField()
	public double quantity;

	@DatabaseField()
	public String herb;

	@DatabaseField()
	public String unit;
	
	// 炮制
	@DatabaseField(width = 300)
	public String comment;

	@DatabaseField(canBeNull = false, foreign = true)
	public YiAnPrescriptionEntity yian;

}
