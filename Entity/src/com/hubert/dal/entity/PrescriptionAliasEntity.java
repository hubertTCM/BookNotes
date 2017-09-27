package com.hubert.dal.entity;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

@DatabaseTable(tableName = "prescriptionalias")
public class PrescriptionAliasEntity {

    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(width = 100)
    public String name;

    @DatabaseField(width = 100)
    public String alias;

    // prescription name is duplicate some times.
    @DatabaseField(canBeNull = false, foreign = true)
    public PrescriptionEntity prescription;
}
