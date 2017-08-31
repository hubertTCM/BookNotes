package com.hubert.dataprovider.parser.tokenextractor;

public enum TokenType {
	None,
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
	Unknown,
	End,
}
