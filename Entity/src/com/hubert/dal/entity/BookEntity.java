package com.hubert.dal.entity;

import java.util.Collection;

//import com.j256.ormlite.dao.*;
import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

@DatabaseTable(tableName = "book")
public class BookEntity {

	@DatabaseField(generatedId = true)
	public long id;

	@DatabaseField(width = 256)
	public String name;

	@ForeignCollectionField(eager = false)
	public Collection<SectionEntity> sections;
	//public ForeignCollection<SectionEntity> sections;
}
