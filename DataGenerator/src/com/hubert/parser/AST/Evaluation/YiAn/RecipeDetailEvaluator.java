package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.*;
import com.hubert.parser.AST.YiAn.*;
import com.hubert.parser.tokenextractor.Position;

public class RecipeDetailEvaluator extends AbstractEvaluator {

    public RecipeDetailEvaluator(Context context) {
        super(YiAnNodeConstants.RecipeDetail, context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        YiAnPrescriptionEntity prescription = new YiAnPrescriptionEntity();
        prescription.items = new ArrayList<YiAnPrescriptionItemEntity>();
        mYiAnScope.setYiAnPrescription(prescription);
       
        
        mYiAnScope.createBlockCreator(BlockTypeEnum.YiAnPrescription);
        return true;
    }

    @Override
    protected boolean postEvaluateCore(ASTNode node) {
        YiAnPrescriptionEntity prescription = mYiAnScope.getYiAnPrescription();
        ArrayList<String> herbs = new ArrayList<String>();
        for (YiAnPrescriptionItemEntity item : prescription.items) {
            HerbAliasManager herbAliasManager = mYiAnScope.getHerbAliasManager();
            String standardName = herbAliasManager.getStandardName(item.herb);
            if (!herbs.contains(standardName)) {
                herbs.add(standardName);
            }
        }
        Collections.sort(herbs, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        prescription.summary = "";
        for (String herb : herbs) {
            prescription.summary += " " + herb;
        }
        prescription.summary = StringUtils.trim(prescription.summary);

        
        BlockCreator blockCreator = mYiAnScope.getBlockCreator();
        SortedMap<Position, BlockEntity> blocks = blockCreator.create();
        for(Map.Entry<Position, BlockEntity> entry : blocks.entrySet()){
            BlockEntity block = entry.getValue();
            if (block.blockType.equals(BlockTypeEnum.ParserText.name())){
                continue;
            }
            
            prescription.block = block;
            break;
        }
        
        return true;
    }

}
