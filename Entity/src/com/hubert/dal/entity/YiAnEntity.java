package com.hubert.dal.entity;

import java.util.*;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

//医案
// TODO: delete. Replaced by BlockGroup
@DatabaseTable(tableName = "yian")
public class YiAnEntity {
    @DatabaseField(generatedId = true)
    public long id;

    @ForeignCollectionField(eager = false)
    public Collection<YiAnDetailEntity> details;
}
