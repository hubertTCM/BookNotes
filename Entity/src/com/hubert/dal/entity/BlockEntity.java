package com.hubert.dal.entity;


import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

// block is the minimal unit of each book ( others)
//   The other data type (like 医案，医话) links to block directly.
@DatabaseTable(tableName = "block")
public class BlockEntity {
    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(canBeNull = true, foreign = true)
    public SectionEntity section;

    @DatabaseField(canBeNull = true, foreign = true)
    public BlockGroupEntity blockGroup;

    @DatabaseField(width = 3000)
    public String content;

    @DatabaseField(width = 256)
    public String blockType;

    @DatabaseField()
    public long order;

}
