package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;
import java.util.Map.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.tokenextractor.*;

public class BlockCreator {
    public BlockCreator(BlockTypeEnum blockType, DataProvider dataProvider, BlockPositionManager positionManager,
            SectionEntity section) {
        mBlockType = blockType;
        mDataProvider = dataProvider;
        mPositionManager = positionManager;
        mSection = section;
    }

    public SortedMap<Position, BlockEntity> create() {
        SortedMap<Position, BlockEntity> blocks = new TreeMap<Position, BlockEntity>();
        if (mTokenTypes.isEmpty() || mDataProvider == null) {
            return blocks;
        }

        String blockContent = "";
        for (Entry<Position, String> entry : mTokenTypes.entrySet()) {
            ContentType contentType = mDataProvider.getContentType(entry.getKey());
            String content = mDataProvider.getContent(entry.getKey());
            if (contentType.equals(ContentType.AdjustedContentForParser)) {
                //blocks.put(entry.getKey(), createBlock(content, BlockTypeEnum.ParserText));
                continue;
            }
            blockContent += content + "\n";
        }

        blocks.put(mTokenTypes.firstKey(), createBlock(blockContent, mBlockType));
        
        for(Entry<Position, BlockEntity> temp : blocks.entrySet()){
            mPositionManager.setPosition(temp.getValue(), temp.getKey());
        }

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
    private BlockPositionManager mPositionManager;
    private SectionEntity mSection;
    private SortedMap<Position, String> mTokenTypes = new TreeMap<Position, String>();
}
