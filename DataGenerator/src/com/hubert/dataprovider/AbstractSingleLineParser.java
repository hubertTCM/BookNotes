package com.hubert.dataprovider;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractSingleLineParser {
	public AbstractSingleLineParser(List<String> adjustedTexts) {
		mAdjustedTexts = adjustedTexts;
	}

	public abstract AbstractSingleLineParser parse(String line);

	protected List<String> mAdjustedTexts;
}
