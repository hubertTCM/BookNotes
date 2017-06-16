package com.hubert.dataprovider;

import java.util.*;

import com.hubert.dal.entity.*;

public class YiAnDetailParser extends AbstractSingleLineParser {
	enum State{
		Init,
		BeginParsePrescription,
		ParsingPrescription,
	}
	
	public YiAnDetailParser(YiAnParser yianParser, List<String> adjustedTexts) {
		super(adjustedTexts);
		mYiAnParser = yianParser;
	}

	@Override
	public void internalParse(String line) {
		// line is not empty here. 
		// start of yian,  or 
		// line start with "又"
		
		if (mCurrentYiAnDetail != null) {
			mYiAn.details.add(mCurrentYiAnDetail);
		}
		mCurrentYiAnDetail = new YiAnDetailEntity();
		
		this.mCurrentState = State.BeginParsePrescription;
	}
	
	@Override
	public AbstractSingleLineParser getNext(){
		if (this.mCurrentState == State.BeginParsePrescription){
			this.mCurrentState = State.ParsingPrescription;
			
			// to prescription.
		}
		if (this.mCurrentState == State.ParsingPrescription){
			// prescription is ready.
			return mYiAnParser;
		}
		return null;
	}
	
	public static boolean isNextYiAnDetail(String line){
		return line.indexOf("又") == 0;
	}

	//private SectionEntity mParentSection;
	private YiAnDetailEntity mCurrentYiAnDetail;
	private YiAnEntity mYiAn;
	private State mCurrentState = State.Init;
	private YiAnParser mYiAnParser;
}
