package com.hubert.parser.AST.YiAn;

import com.hubert.parser.tokenextractor.YiAn.YiAnTokenType;

public class YiAnNodeConstants {
    public static final String YN = "YN"; // 医案
    public static final String YN2 = "YN2";
    public static final String YiAnDetail = "YNDetail"; // 一诊 二诊 ...
    public static final String YNDetail2 = "YNDetail2";
    public static final String Description = YiAnTokenType.Description.name();
    public static final String RecipeDetail = "RecipeDetail";
    public static final String RecipeContent = YiAnTokenType.RecipeContent.name();
    //public static final String RecipeComposition = "RecipeComposition";
    public static final String RecipeCompositionHerbOnly = "RecipeCompositionHerbOnly";
    public static final String RecipeAbbreviation = YiAnTokenType.RecipeAbbreviation.name();
    public static final String RecipeHeader = YiAnTokenType.RecipeHeader.name();
    public static final String RecipeComment = YiAnTokenType.RecipeComment.name();
    public static final String Herb = YiAnTokenType.Herb.name(); 

    public static final String SummaryComment = YiAnTokenType.SummaryComment.name();
    public static final String SectionName = YiAnTokenType.SectionName.name();
}
