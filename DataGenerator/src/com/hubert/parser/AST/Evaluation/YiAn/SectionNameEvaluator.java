package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.Evaluation.Common.*;
import com.hubert.parser.AST.YiAn.*;

public class SectionNameEvaluator extends AbstractEvaluator{

    public SectionNameEvaluator(Context context) {
        super(YiAnNodeConstants.SectionName, context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        SectionEntity entity = new SectionEntity();
        entity.blocks = new ArrayList<BlockEntity>();
        entity.childSections = new ArrayList<SectionEntity>();
        entity.parent = mYiAnScope.getRootSection();
        if (entity.parent == null){
            entity.book = mYiAnScope.getBook();
            entity.order = entity.book.sections.size();
        }
        else{
            entity.order = entity.parent.childSections.size();
        }
        mContext.setGlobalData(YiAnScope.ActiveSectionEntityKey, entity);
        return true;
    }

}
