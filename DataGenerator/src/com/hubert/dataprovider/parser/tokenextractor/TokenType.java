package com.hubert.dataprovider.parser.tokenextractor;

public enum TokenType {
	None,
	Start,
	SectionName,
	YiAnDescription,
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
	BlankSpace,
	LiteralText,
	End,
}
