package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;
import java.util.Map.Entry;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.Evaluation.Common.*;
import com.hubert.parser.AST.YiAn.*;
import com.hubert.parser.tokenextractor.*;

public class BlockCreator<T> implements IBlockCreator{
    public BlockCreator(T data, YiAnScope yiAnScope) {
        mData = data;
        mYiAnScope = yiAnScope;
    }

    public T get() {
        return mData;
    }

    public List<BlockEntity> create() {
        List<BlockEntity> blocks = new ArrayList<BlockEntity>();

//        for(IBlockCreator propertyBlockCreator : mPropertyBlockCreators){
//            blocks.addAll(propertyBlockCreator.create());
//        }
        
        if (mTokenTypes.isEmpty()) {
            return blocks;
        }
        BlockEntity entity = new BlockEntity();
        entity.content = "";
        
        blocks.add(0, entity);

        DataProvider provider = mYiAnScope.getDataProvider();
        if (provider == null) {
            return blocks;
        }
        String previousToken = null;
        for (Entry<Position, String> entry : mTokenTypes.entrySet()) {
            if (YiAnNodeConstants.RecipeAbbreviation.equals(previousToken)) {
                previousToken = entry.getValue();
                continue;
            }
            previousToken = entry.getValue();
            String content = provider.getContent(entry.getKey());
            entity.content += content + "\n";
        }
        
        SectionEntity section = mYiAnScope.getActiveSection();
        entity.section = section;
        section.blocks.add(entity);
        entity.order = section.blocks.size();

        return blocks;
    }

    public void setParent(IBlockCreator parent){
        
    }
    
    public boolean addToken(Position position) {
        String tokenType = mYiAnScope.getNodeType();
        return addToken(position, tokenType);
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

    private T mData;
    private YiAnScope mYiAnScope;
    private SortedMap<Position, String> mTokenTypes = new TreeMap<Position, String>();
}
