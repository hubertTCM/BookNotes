package com.hubert.dataprovider.parser.tokenextractor;

public enum TokenType {
	None,
	SectionName,
	YiAnDescription,
	//NewYiAnDescription,
	PrescriptionHeader, // RH
	PrescriptionAbbreviation,
	PrescriptionFormatted,
	Herb,
	HerbQuantity,
	Unit,
	HerbComment,
	PrescriptionComment,
	YiAnComment,
	SummaryComment,
	BlankSpace,
}
