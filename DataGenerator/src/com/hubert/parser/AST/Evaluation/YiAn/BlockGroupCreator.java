package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;
import java.util.Map.Entry;

import com.hubert.dal.entity.*;
import com.hubert.parser.tokenextractor.Position;

public class BlockGroupCreator {

    public BlockGroupCreator(BookEntity book, BlockGroupCreator parentCreator, BlockGroupTypeEnum blockGroupType,
            BlockPositionManager positionManager, SortedMap<Position, String> tokensContainer) {

        mTokensContainer = tokensContainer;
        mPositionManager = positionManager;

        mBlockGroup = new BlockGroupEntity();
        mBlockGroup.type = blockGroupType;
        mBlockGroup.book = book;

        if (parentCreator != null) {
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
        mBlockGroup.children = new Vector<BlockGroupEntity>();

        BlockEntity previousBlock = null;
        for (BlockCreator creator : mBlockCreators) {
            SortedMap<Position, BlockEntity> blocks = creator.create();
            mTokensContainer.putAll(creator.getTokenTypes());

            PrescriptionEntity blockReference = creator.getBlockReference();
            if (blockReference != null) {
                if (blocks.isEmpty()) {
                    blockReference.block = previousBlock;
                    continue;
                }
                blockReference.block = null;
            }

            for (Entry<Position, BlockEntity> entry : blocks.entrySet()) {
                previousBlock = addBlock(entry.getKey(), entry.getValue());

                if (blockReference != null && blockReference.block == null) {
                    blockReference.block = previousBlock;
                }
            }
        }

        for (BlockGroupCreator child : mChildBlockGroupCreators) {
            BlockGroupEntity temp = child.create();
            temp.parent = mBlockGroup;
            mBlockGroup.children.add(temp);
        }

        return mBlockGroup;
    }

    private BlockEntity addBlock(Position position, BlockEntity block) {
        for (BlockEntity existing : mBlockGroup.blocks) {
            if (block.content.equals(existing.content)) {
                remove(block);
                return existing;
            }
            Position existingPosition = mPositionManager.getPosition(existing);
            if (existingPosition.compareTo(position) == 0) {
                remove(block);
                return existing;
            }
        }

        block.blockGroup = mBlockGroup;
        mBlockGroup.blocks.add(block);
        mPositionManager.setPosition(block, position);
        return block;
    }

    private void remove(BlockEntity block) {
        if (block.section != null) {
            block.section.blocks.remove(block);
        }
    }

    private List<BlockCreator> mBlockCreators = new Vector<BlockCreator>();
    private BlockPositionManager mPositionManager;
    private List<BlockGroupCreator> mChildBlockGroupCreators = new Vector<BlockGroupCreator>();
    SortedMap<Position, String> mTokensContainer;
    private BlockGroupEntity mBlockGroup;
}
