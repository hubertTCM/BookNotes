package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;
import java.util.Map.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.tokenextractor.*;

public class BlockCreator {
    public BlockCreator(BlockTypeEnum blockType, DataProvider dataProvider, SectionEntity section) {
        mBlockType = blockType;
        mDataProvider = dataProvider;
        mSection = section;
    }

    public SortedMap<Position, BlockEntity> create() {
        SortedMap<Position, BlockEntity> blocks = new TreeMap<Position, BlockEntity>();
        String blockContent = "";
        for (Entry<Position, String> entry : mTokenTypes.entrySet()) {
            ContentType contentType = mDataProvider.getContentType(entry.getKey());
            String content = mDataProvider.getContent(entry.getKey());
            if (contentType.equals(ContentType.AdjustedContentForParser)) {
                blocks.put(entry.getKey(), createBlock(content, BlockTypeEnum.ParserText));
                continue;
            }
            blockContent += content + "\n";
        }

        blocks.put(mTokenTypes.firstKey(), createBlock(blockContent, mBlockType));

        return blocks;
    }

    private BlockEntity createBlock(String blockContent, BlockTypeEnum blockType) {
        BlockEntity entity = new BlockEntity();
        entity.blockType = blockType.name();
        entity.content = blockContent;

        mSection.blocks.add(entity);
        entity.section = mSection;
        return entity;
    }

    public boolean addToken(Position position, String tokenType) {
        if (!mTokenTypes.containsKey(position)) {
            mTokenTypes.put(position, tokenType);
            return true;
        }

        String existingContent = mTokenTypes.get(position);
        return existingContent.equals(tokenType);
    }

    public SortedMap<Position, String> getTokenTypes() {
        return mTokenTypes;
    }

    private BlockTypeEnum mBlockType;
    private DataProvider mDataProvider;
    private SectionEntity mSection;
    private SortedMap<Position, String> mTokenTypes = new TreeMap<Position, String>();
}
