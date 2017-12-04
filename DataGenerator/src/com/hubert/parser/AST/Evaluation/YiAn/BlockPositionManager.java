package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.tokenextractor.*;

import javafx.util.Pair;

public class BlockPositionManager {
    public Position getPosition(BlockEntity entity) {
        for (Pair<BlockEntity, Position> pair : mBlockPositions) {
            if (pair.getKey().equals(entity)) {
                return pair.getValue();
            }
        }
        return null;
    }

    public void setPosition(BlockEntity entity, Position position) {
        Pair<BlockEntity, Position> pair = new Pair<BlockEntity, Position>(entity, position);
        mBlockPositions.add(pair);
    }

    private List<Pair<BlockEntity, Position>> mBlockPositions = new Vector<Pair<BlockEntity, Position>>();
}
