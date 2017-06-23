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
			if (mCurrentYiAnDetail == null) {
				System.out.println("## error");
			}
			return mYiAnParser.parse(line);
		}
		
		mCurrentYiAnDetail = new YiAnDetailEntity();
		mCurrentYiAnDetail.order = mYiAn.details.size() + 1;
		mCurrentYiAnDetail.prescriptions = new ArrayList<YiAnPrescriptionEntity>();
		mCurrentYiAnDetail.yian = mYiAn;
		mYiAn.details.add(mCurrentYiAnDetail);
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
		if( line.indexOf("又") == 0){
			return true;
		}
		return false;
	}

	//private SectionEntity mParentSection;
	private YiAnDetailEntity mCurrentYiAnDetail;
	private YiAnEntity mYiAn;
	//private State mCurrentState = State.Init;
	private YiAnParser mYiAnParser;
}
