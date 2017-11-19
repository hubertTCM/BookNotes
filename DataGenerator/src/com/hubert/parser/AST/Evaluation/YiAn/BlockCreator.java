package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;
import java.util.Map.Entry;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.Evaluation.Common.*;
import com.hubert.parser.AST.YiAn.*;
import com.hubert.parser.tokenextractor.*;

public class BlockCreator<T> implements IBlockCreator {
    public BlockCreator(T entity, String blockType, YiAnScope yiAnScope) {
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
        BlockEntity entity = new BlockEntity();
        entity.blockType = mBlockType;
        entity.content = "";
        for (Entry<Position, String> entry : mTokenTypes.entrySet()) {
            ContentType contentType = provider.getContentType(entry.getKey());
            if (contentType.equals(ContentType.AdditionalText)){
                continue;
            }
            String content = provider.getContent(entry.getKey());
            entity.content += content + "\n";
        }

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
        mPropertyBlocks.add(blocks);
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
        List<BlockEntity> blocks = new ArrayList<BlockEntity>();
        for (int i = 0; i < mPropertyBlocks.size(); i++) {
            blocks.addAll(mPropertyBlocks.get(i));
        }
        blocks.add(block);
        block.order = blocks.size();
        return blocks;
    }

    private T mEntity;
    private String mBlockType;
    private YiAnScope mYiAnScope;
    private IBlockCreator mParent;
    private List<List<BlockEntity>> mPropertyBlocks = new ArrayList<List<BlockEntity>>();
    private SortedMap<Position, String> mTokenTypes = new TreeMap<Position, String>();
}
