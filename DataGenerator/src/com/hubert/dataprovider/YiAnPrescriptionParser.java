package com.hubert.dataprovider;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;

public class YiAnPrescriptionParser extends AbstractSingleLineParser {
	public YiAnPrescriptionParser(YiAnDetailParser yianDetailParser, List<String> adjustedTexts) {
		super(adjustedTexts);
		mYiAnDetailParser = yianDetailParser;
		
		mYiAnPrescriptionEntity = new YiAnPrescriptionEntity();
		mYiAnPrescriptionEntity.items = new ArrayList<YiAnPrescriptionItemEntity>();
	}

	@Override
	public void internalParse(String line) {
	
	}

	@Override
	protected boolean isEnd(String line){
		return super.isEnd(line) || YiAnDetailParser.isNextYiAnDetail(line);
	}
	
	@Override
	protected AbstractSingleLineParser getNext(){
		return mYiAnDetailParser;
	}
	
	private YiAnDetailParser mYiAnDetailParser;
	private YiAnPrescriptionEntity mYiAnPrescriptionEntity;
}
