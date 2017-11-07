package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;
import java.util.Map.Entry;

import com.hubert.dal.entity.*;
import com.hubert.parser.tokenextractor.*;

public class BlockGenerator<T> {
    public BlockGenerator(T data, YiAnScope yiAnScope) {
        mData = data;
        mYiAnScope = yiAnScope;
    }

    public T get() {
        return mData;
    }

    public BlockEntity createBlock() {
        if (mTokenTypes.isEmpty()) {
            return null;
        }
        BlockEntity entity = new BlockEntity();
        entity.content = "";

        DataProvider provider = mYiAnScope.getDataProvider();
        if (provider == null){
            return null;
        }
        for (Entry<Position, String> entry : mTokenTypes.entrySet()) {
            entity.content += provider.getContent(entry.getKey()) + "\n";
        }

        return entity;
    }

    public boolean addToken(Position position) {
        String tokenType = mYiAnScope.getNodeType();
        return addToken(position, tokenType);
    }

    public boolean addToken(Position position, String tokenType) {
        if (!mTokenTypes.containsKey(position)) {
            mTokenTypes.put(position, tokenType);
            
            SortedMap<Position, String> temp = mYiAnScope.getOriginalTokens();
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
