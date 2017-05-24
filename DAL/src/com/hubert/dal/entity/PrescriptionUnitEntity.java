package com.hubert.dal.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "prescriptionunit")
public class PrescriptionUnitEntity {
	@DatabaseField(generatedId = true)
	public long id;

	@DatabaseField(width = 100)
	public String herb;

	public double quantity;

	public String unit;

	// 炮制
	@DatabaseField(width = 1500)
	public String comment;

	@DatabaseField(canBeNull = true, foreign = true)
	public PrescriptionEntity prescription;
}
