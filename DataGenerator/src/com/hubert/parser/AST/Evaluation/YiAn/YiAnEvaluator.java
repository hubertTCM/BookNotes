package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.YiAnNodeConstants;

public class YiAnEvaluator extends AbstractEvaluator {

    public YiAnEvaluator(Context context, List<YiAnEntity> yiAns) {
        super(Arrays.asList(YiAnNodeConstants.YN, YiAnNodeConstants.YN2), context);
        mYiAns = yiAns;
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        YiAnEntity yiAn = new YiAnEntity();
        yiAn.details = new ArrayList<YiAnDetailEntity>();
        mYiAnScope.setYiAn(yiAn);
        mYiAns.add(yiAn);

        mYiAnScope.initCurrentTokens();
        return true;
    }

    // TODO: it is tricky here. can be implemented better.
//    @Override
//    public boolean postEvaluate(ASTNode node) {
//        //SectionEntity section = mYiAnScope.getActiveSection();
//        //YiAnEntity yiAn = mYiAnScope.getYiAn().get();
//        YiAnEntity yiAn = mYiAnScope.getYiAn();
//        List<YiAnDetailBlockLinkEntity> tempLinks = new ArrayList<YiAnDetailBlockLinkEntity>();
//        for (YiAnDetailEntity detail : yiAn.details) {
//            tempLinks.clear();
//
//            tempLinks.addAll(detail.blockLinks);
//
//
//            for (int j = tempLinks.size() - 1; j > 0; j--) {
//                YiAnDetailBlockLinkEntity linkJ = tempLinks.get(j);
//                for (int i = 0; i < j; i++) {
//                    YiAnDetailBlockLinkEntity linkI = tempLinks.get(i);
//                    if (linkJ.block.content.equals(linkI.block.content)) {
//                        tempLinks.remove(j);
//                        detail.blockLinks.remove(linkJ);
//                    }
//                }
//            }
//
//            /*
//            for (YiAnDetailBlockLinkEntity link : tempLinks) {
//                BlockEntity temp = link.block;
//                temp.section = section;
//                section.blocks.add(temp);
//                temp.order = section.blocks.size();
//            }
//            */
//        }
//        return true;
//    }

    private List<YiAnEntity> mYiAns = null;// new ArrayList<YiAnEntity>();
}
