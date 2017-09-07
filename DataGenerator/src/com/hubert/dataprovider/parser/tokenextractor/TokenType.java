package com.hubert.dataprovider.parser.tokenextractor;

public enum TokenType {
	None,
	S,
	SectionName,
	Description,
	//NewYiAnDescription,
	RecipeHeaderHeader, // RH
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
