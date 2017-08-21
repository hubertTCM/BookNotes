package com.hubert.dataprovider.parser;

public enum TokenType {
	None,
	SectionName,
	YiAnDescription,
	PrescriptionHeader, // RH
	PrescriptionAbbreviation,
	PrescriptionFormatted,
	PrescriptionComment,
	YiAnComment,
	SummaryComment,
	BlankSpace,
}
