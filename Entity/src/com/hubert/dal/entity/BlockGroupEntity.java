package com.hubert.dal.entity;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

@DatabaseTable(tableName = "blockgroup")
public class BlockGroupEntity {
    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    BlockGroupTypeEnum type;
}
