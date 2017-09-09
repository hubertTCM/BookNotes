package com.hubert.parser.tokenextractor;

public enum TokenType {
	None,
	S,
	SectionName,
	Description,
	RecipeHeader, 
	RecipeAbbreviation,
	FormattedRecipeText,
	RecipeComment,
	YiAnComment,
	Herb,
	HerbQuantity,
	Unit,
	HerbComment,
	SummaryComment,
	//BlankSpace,
	LiteralText,
	End,
}
