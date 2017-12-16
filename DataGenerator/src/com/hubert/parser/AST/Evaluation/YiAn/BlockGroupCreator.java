package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;
import java.util.Map.Entry;

import com.hubert.dal.entity.*;
import com.hubert.parser.tokenextractor.Position;

public class BlockGroupCreator {

    public BlockGroupCreator(BlockGroupCreator parentCreator, BlockGroupTypeEnum blockGroupType,
            BlockPositionManager positionManager, SortedMap<Position, String> tokensContainer) {

        mTokensContainer = tokensContainer;
        mPositionManager = positionManager;

        mBlockGroup = new BlockGroupEntity();
        mBlockGroup.type = blockGroupType;
        
        if (parentCreator != null){
            parentCreator.mChildBlockGroupCreators.add(this);
        }
    }

    public boolean add(BlockCreator creator) {
        return mBlockCreators.add(creator);
    }

    public BlockGroupEntity create() {
        if (mBlockGroup.blocks != null) {
            return mBlockGroup;
        }
        mBlockGroup.blocks = new Vector<BlockEntity>();

        for (BlockCreator creator : mBlockCreators) {
            SortedMap<Position, BlockEntity> blocks = creator.create();
            mTokensContainer.putAll(creator.getTokenTypes());
            for (Entry<Position, BlockEntity> entry : blocks.entrySet()) {
                addBlock(entry.getKey(), entry.getValue());
            }
        }

        for (BlockGroupCreator child : mChildBlockGroupCreators) {
            BlockGroupEntity temp = child.create();
            temp.parent = mBlockGroup;
        }

        return mBlockGroup;
    }

    private void addBlock(Position position, BlockEntity blockEntity) {
        for (BlockEntity existing : mBlockGroup.blocks) {
            if (blockEntity.content.equals(existing.content)) {
                return;
            }
            Position existingPosition = mPositionManager.getPosition(existing);
            if (existingPosition.compareTo(position) == 0) {
                return;
            }
        }

        mBlockGroup.blocks.add(blockEntity);
        mPositionManager.setPosition(blockEntity, position);
    }

    private List<BlockCreator> mBlockCreators = new Vector<BlockCreator>();
    private BlockPositionManager mPositionManager;
    private List<BlockGroupCreator> mChildBlockGroupCreators = new Vector<BlockGroupCreator>();
    SortedMap<Position, String> mTokensContainer;
    private BlockGroupEntity mBlockGroup;
}
