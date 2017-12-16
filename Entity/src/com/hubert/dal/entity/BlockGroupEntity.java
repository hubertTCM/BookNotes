package com.hubert.dal.entity;

import java.util.Collection;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;

// YiAn YiAnDetail一诊， 二诊
@DatabaseTable(tableName = "blockgroup")
public class BlockGroupEntity {
    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    public BlockGroupTypeEnum type;

    @DatabaseField(canBeNull = true, foreign = true)
    public BlockGroupEntity parent;

    // yes, the book can be found by Block.Section.Book
    // keep here for shortcut
    @DatabaseField(canBeNull = false, foreign = true)
    public BookEntity book;

    @ForeignCollectionField(eager = false)
    public Collection<BlockEntity> blocks;
}
