package com.hubert.dal.entity;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

@DatabaseTable(tableName = "prescriptionblocklink")
public class PrescriptionBlockLinkEntity {
	@DatabaseField(generatedId = true)
	public long id;

	@DatabaseField(canBeNull = true, foreign = true)
	public BlockEntity block;

	@DatabaseField(canBeNull = true, foreign = true)
	public PrescriptionEntity prescription;
}
