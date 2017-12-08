package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;
import java.util.Map.Entry;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.Evaluation.Common.*;
import com.hubert.parser.tokenextractor.*;

// TODO: BlockCreator should not depend on YiAnScope, as YiAnScope is destroyed
//   after the single node is visited.
public class BlockLinkCreator<T> implements IBlockCreator {
    public BlockLinkCreator(T entity, String blockType, YiAnScope yiAnScope) {
        mEntity = entity;
        mBlockType = blockType;
        mYiAnScope = yiAnScope;
    }

    public T getEntity() {
        return mEntity;
    }

    public List<BlockEntity> create() {
        DataProvider provider = mYiAnScope.getDataProvider();
        if (mTokenTypes.isEmpty() || provider == null) {
            return new ArrayList<BlockEntity>();
        }
        String blockContent = "";
        for (Entry<Position, String> entry : mTokenTypes.entrySet()) {
            ContentType contentType = provider.getContentType(entry.getKey());
            if (contentType.equals(ContentType.AdditionalText)) {
                continue;
            }
            String content = provider.getContent(entry.getKey());
            blockContent += content + "\n";
        }

        if (blockContent.isEmpty()) {
            return mPropertyBlocks;
        }

        BlockEntity entity = new BlockEntity();
        entity.blockType = mBlockType;
        entity.content = blockContent;

        SectionEntity section = mYiAnScope.getActiveSection();
        section.blocks.add(entity);
        entity.section = section;

        BlockPositionManager positionManager = mYiAnScope.getBlockPositionManager();
        positionManager.setPosition(entity, mTokenTypes.firstKey());

        List<BlockEntity> blocks = getAllBlocks(entity);
        if (mParent != null) {
            mParent.addPropertyBlock(blocks);
        }

        return blocks;
    }

    public void setParent(IBlockCreator parent) {
        mParent = parent;
    }

    public void addPropertyBlock(List<BlockEntity> blocks) {
        mPropertyBlocks.addAll(blocks);
    }

    public boolean addToken(Position position, String tokenType) {
        if (!mTokenTypes.containsKey(position)) {
            mTokenTypes.put(position, tokenType);

            SortedMap<Position, String> temp = mYiAnScope.getCurrentTokens();
            temp.put(position, tokenType);

            return true;
        }

        String existingContent = mTokenTypes.get(position);
        return existingContent.equals(tokenType);
    }

    private List<BlockEntity> getAllBlocks(BlockEntity block) {
        String content = block.content;
        List<BlockEntity> blocks = new ArrayList<BlockEntity>();
        // avoid 又 生地 阿胶 牡蛎 川斛 知母 twice. One in prescription, one in recipe
        // description.
        for (BlockEntity temp : mPropertyBlocks) {
            if (content.equals(temp.content)) {
                remove(temp);
                continue;
            }
            blocks.add(temp);
        }

        blocks.add(block);
        block.order = blocks.size();

        return blocks;
    }

    private void remove(BlockEntity block) {
        SectionEntity section = block.section;
        if (section == null) {
            return;
        }
        section.blocks.remove(block);
        block.section = null;
    }

    private T mEntity;
    private String mBlockType;
    private YiAnScope mYiAnScope;
    private IBlockCreator mParent;
    private List<BlockEntity> mPropertyBlocks = new ArrayList<BlockEntity>();
    private SortedMap<Position, String> mTokenTypes = new TreeMap<Position, String>();
}
