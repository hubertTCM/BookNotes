package com.hubert.dataprovider;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;

public class YiAnPrescriptionParser extends AbstractSingleLineParser {

	public YiAnPrescriptionParser(YiAnDetailParser yianDetailParser, List<String> adjustedTexts) {
		super(adjustedTexts);
		mPrescriptionItemParser = new PrescriptionItemsParser();
		
		mYiAnDetailParser = yianDetailParser;

		mYiAnPrescriptionEntity = new YiAnPrescriptionEntity();
		mYiAnPrescriptionEntity.items = new ArrayList<YiAnPrescriptionItemEntity>();
	}

	@Override
	public AbstractSingleLineParser parse(String line) {
		if (line.isEmpty() || YiAnDetailParser.isNextYiAnDetail(line)) {
			return mYiAnDetailParser.parse(line);
		}
		
		if (mYiAnPrescriptionEntity.items.isEmpty()){
			Collection<PrescriptionItemEntity> items = mPrescriptionItemParser.parse(line);
			for(PrescriptionItemEntity temp : items){
				YiAnPrescriptionItemEntity entity = new YiAnPrescriptionItemEntity();
				entity.herb = temp.herb;
				entity.quantity = temp.quantity;
				entity.unit = temp.unit;
				entity.comment = temp.comment;
				entity.yian = mYiAnPrescriptionEntity;
				mYiAnPrescriptionEntity.items.add(entity);
			}
			// first time
			return this;
		}

		mYiAnPrescriptionEntity.comment = mYiAnPrescriptionEntity.comment + " " + line; 
		return this;
	}

	private PrescriptionItemsParser mPrescriptionItemParser;
	private YiAnDetailParser mYiAnDetailParser;
	private YiAnPrescriptionEntity mYiAnPrescriptionEntity;
}