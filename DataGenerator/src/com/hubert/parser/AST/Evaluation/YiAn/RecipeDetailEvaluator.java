package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.dto.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.*;
import com.hubert.parser.AST.YiAn.*;

public class RecipeDetailEvaluator extends AbstractEvaluator {

    public RecipeDetailEvaluator(Context context) {
        super(YiAnNodeConstants.RecipeDetail, context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        Prescription prescription = new Prescription();
        mYiAnScope.setYiAnPrescription(prescription);
       
        
        mYiAnScope.createBlockCreator(BlockTypeEnum.YiAnPrescription, prescription.getEntity());
        return true;
    }

    @Override
    protected boolean postEvaluateCore(ASTNode node) {
        Prescription prescription = mYiAnScope.getYiAnPrescription();
        ArrayList<String> herbs = new ArrayList<String>();
        Collection<PrescriptionItem> items = prescription.getItems();
        for (PrescriptionItem item : items) {
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
        String summary = "";
        for (String herb : herbs) {
            summary += " " + herb;
        }
        summary = StringUtils.trim(summary);
        prescription.setSummary(summary);

        
//        BlockCreator blockCreator = mYiAnScope.getBlockCreator();
//        SortedMap<Position, BlockEntity> blocks = blockCreator.create();
//        for(Map.Entry<Position, BlockEntity> entry : blocks.entrySet()){
//            BlockEntity block = entry.getValue();
//            if (block.blockType.equals(BlockTypeEnum.ParserText.name())){
//                continue;
//            }
//            
//            prescription.block = block;
//            break;
//        }
        
//        // TODO: 
//        if (prescription.block == null){
//            List<PrescriptionEntity> prescriptions = mYiAnScope.getPrescriptions();
//            prescriptions.remove(prescription);
//        }
        
        return true;
    }

}
