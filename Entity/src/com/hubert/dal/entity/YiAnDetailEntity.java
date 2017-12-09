package com.hubert.dal.entity;

import java.util.*;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

// 初诊 二诊 三诊
// TODO: delete, replaced by BlockGroup
@DatabaseTable(tableName = "yiandetail")
public class YiAnDetailEntity {
    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(width = 3000)
    public String content;

    @DatabaseField(width = 3000)
    public String comment;

    @DatabaseField()
    public long order;

    @DatabaseField(canBeNull = false, foreign = true)
    public YiAnEntity yian;

    @ForeignCollectionField(eager = false)
    public Collection<YiAnPrescriptionEntity> prescriptions;

    @ForeignCollectionField(eager = false)
    public Collection<YiAnDetailBlockLinkEntity> blockLinks;
}
