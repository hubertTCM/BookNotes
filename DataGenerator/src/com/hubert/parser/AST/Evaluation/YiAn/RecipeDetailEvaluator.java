package com.hubert.parser.AST.Evaluation.YiAn;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.HerbAliasManager;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.Evaluation.Common.Context;
import com.hubert.parser.AST.YiAn.*;

public class RecipeDetailEvaluator extends AbstractEvaluator {

    public RecipeDetailEvaluator(Context context) {
        super(YiAnNodeConstants.RecipeDetail, context);
    }

    @Override
    protected boolean evaluateCore(ASTNode node) {
        EntityProvider<YiAnDetailEntity> temp = mYiAnScope.getYiAnDetail();
        YiAnDetailEntity yiAnDetail = temp.get();
        YiAnPrescriptionEntity prescription = new YiAnPrescriptionEntity();
        prescription.items = new ArrayList<YiAnPrescriptionItemEntity>();
        prescription.order = yiAnDetail.prescriptions.size();
        yiAnDetail.prescriptions.add(prescription);

        mYiAnScope.setYiAnPrescription(prescription);
        return true;
    }

    @Override
    protected boolean postEvaluateCore(ASTNode node) {
        EntityProvider<YiAnPrescriptionEntity> temp = mYiAnScope.getYiAnPrescription();
        YiAnPrescriptionEntity prescription = temp.get();
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

        return true;
    }

}
