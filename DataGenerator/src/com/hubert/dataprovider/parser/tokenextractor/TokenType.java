package com.hubert.dataprovider.parser.tokenextractor;

public enum TokenType {
	None,
	SectionName,
	YiAnDescription,
	//NewYiAnDescription,
	PrescriptionHeader, // RH
	PrescriptionAbbreviation,
	PrescriptionFormatted,
	PrescriptionComment,
	YiAnComment,
	SummaryComment,
	BlankSpace,
}
