package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.Evaluation.Common.*;
import com.hubert.parser.AST.YiAn.*;

public class YiAnDetailEvaluator extends AbstractEvaluator {
    public YiAnDetailEvaluator(Context context) {
        super(Arrays.asList(YiAnNodeConstants.YiAnDetail, YiAnNodeConstants.YNDetail2), context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        BlockCreator<YiAnEntity> temp = mYiAnScope.getYiAn();
        YiAnEntity yiAn = temp.get();
        YiAnDetailEntity yiAnDetail = new YiAnDetailEntity();
        yiAnDetail.prescriptions = new ArrayList<YiAnPrescriptionEntity>();
        yiAnDetail.blockLinks = new ArrayList<>();
        yiAnDetail.order = yiAn.details.size() + 1;
        yiAn.details.add(yiAnDetail);

        mYiAnScope.setYiAnDetail(yiAnDetail);
        return true;
    }
    
    @Override
    public boolean destory(ASTNode node) {
        BlockCreator<YiAnDetailEntity> temp = mYiAnScope.getYiAnDetail();
        List<BlockEntity> blocks = temp.create();
        for(BlockEntity block : blocks){
            YiAnDetailEntity yiAnDetail = temp.get();
            YiAnDetailBlockLinkEntity link = new YiAnDetailBlockLinkEntity();
            link.block = block;
            link.yian = yiAnDetail;
            yiAnDetail.blockLinks.add(link);
        }
        return true;
    }

}
