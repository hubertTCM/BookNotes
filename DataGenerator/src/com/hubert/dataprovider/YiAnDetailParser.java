package com.hubert.dataprovider;

import java.util.*;

import com.hubert.dal.entity.*;

public class YiAnDetailParser extends AbstractSingleLineParser {
	public YiAnDetailParser(YiAnParser yianParser, YiAnEntity yian, List<String> adjustedTexts) {
		super(adjustedTexts);
		mYiAn = yian;
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
		mCurrentYiAnDetail.order = mYiAn.details.size() + 1;
		mCurrentYiAnDetail.prescriptions = new ArrayList<YiAnPrescriptionEntity>();
		YiAnPrescriptionParser yiAnPrescriptionParser = new YiAnPrescriptionParser(this, mCurrentYiAnDetail, mAdjustedTexts);
		
		String presciptionOnly  = "又[]";
		int index = line.indexOf(presciptionOnly);
		if (index == 0){
			return yiAnPrescriptionParser.parse(line.substring(presciptionOnly.length(), line.length()));
		}
		mCurrentYiAnDetail.content = line;
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
