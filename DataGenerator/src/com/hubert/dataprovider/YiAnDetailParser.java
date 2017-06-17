package com.hubert.dataprovider;

import java.util.*;

import com.hubert.dal.entity.*;

public class YiAnDetailParser extends AbstractSingleLineParser {
	public YiAnDetailParser(YiAnParser yianParser, List<String> adjustedTexts) {
		super(adjustedTexts);
		mYiAnParser = yianParser;
	}

	@Override
	public AbstractSingleLineParser parse(String line) {		
		if (line.isEmpty()){
			return mYiAnParser.parse(line);
		}
		
		if (mCurrentYiAnDetail != null) {
			mYiAn.details.add(mCurrentYiAnDetail);
		}
		mCurrentYiAnDetail = new YiAnDetailEntity();
		mCurrentYiAnDetail.content = line;
		mCurrentYiAnDetail.prescriptions = new ArrayList<YiAnPrescriptionEntity>();
		
		YiAnPrescriptionParser yiAnPrescriptionParser = new YiAnPrescriptionParser(this, mAdjustedTexts);
		return yiAnPrescriptionParser;
	}
	
	
	public static boolean isNextYiAnDetail(String line){
		return line.indexOf("又") == 0;
	}

	//private SectionEntity mParentSection;
	private YiAnDetailEntity mCurrentYiAnDetail;
	private YiAnEntity mYiAn;
	//private State mCurrentState = State.Init;
	private YiAnParser mYiAnParser;
}
