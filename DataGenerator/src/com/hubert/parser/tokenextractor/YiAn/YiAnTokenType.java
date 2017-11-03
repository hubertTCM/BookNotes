package com.hubert.parser.tokenextractor.YiAn;

public enum YiAnTokenType {
    None, S, SectionName, Description, RecipeHeader, RecipeAbbreviation, FormattedRecipeText, RecipeContent, RecipeComment, YiAnComment, Herb, HerbQuantity, Unit, HerbComment, SummaryComment,
    // BlankSpace,
    LiteralText, End,
}
