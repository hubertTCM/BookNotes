package com.hubert.parser.AST.Evaluation.Common;

import java.util.*;

import com.hubert.dal.entity.*;

public interface IBlockCreator {
    public List<BlockEntity> create();
    public void setParent(IBlockCreator parent);
    public void addPropertyBlock(List<BlockEntity> blocks);
}
