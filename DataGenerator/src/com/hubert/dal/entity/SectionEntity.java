package com.hubert.dal.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

@DatabaseTable(tableName = "section")
public class SectionEntity {

	@DatabaseField(generatedId = true)
	public long id;

	@DatabaseField()
	public String name;

	@DatabaseField()
	public long order;

	@DatabaseField(canBeNull = false, foreign = true)
	public BookEntity book;

	@DatabaseField(canBeNull = true, foreign = true)
	public SectionEntity parent;

	@ForeignCollectionField(eager = true)
	public ForeignCollection<SectionEntity> childSections;

	@ForeignCollectionField(eager = true)
	public ForeignCollection<BlockEntity> blocks;
}
