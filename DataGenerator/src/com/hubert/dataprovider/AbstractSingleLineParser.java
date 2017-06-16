package com.hubert.dataprovider;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractSingleLineParser {
	public AbstractSingleLineParser(List<String> adjustedTexts) {
		// mNext = next;
		mAdjustedTexts = adjustedTexts;
	}

	public void parse(String line) {
		line = StringUtils.strip(line);
		if (isEnd(line)) {
			AbstractSingleLineParser next = getNext();
			if (next != null) {
				next.parse(line);
			}
			return;
		}

		internalParse(line);
	}

	public abstract void internalParse(String line);

	protected boolean isEnd(String line) {
		return line.isEmpty();
	}

	// TODO:
	protected AbstractSingleLineParser getNext() {
		return null;
	}

	protected List<String> mAdjustedTexts;
}
