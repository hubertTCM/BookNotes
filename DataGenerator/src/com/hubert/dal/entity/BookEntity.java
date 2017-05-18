package com.hubert.dal.entity;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

@DatabaseTable(tableName = "book")
public class BookEntity {

	@DatabaseField(generatedId = true)
	public long id;

	@DatabaseField(width = 256)
	public String name;

	@ForeignCollectionField(eager = true)
	public ForeignCollection<SectionEntity> sections;
}
