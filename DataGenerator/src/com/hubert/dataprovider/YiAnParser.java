package com.hubert.dataprovider;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.Repository;
import com.hubert.dal.entity.*;
import com.hubert.training.*;

/* 
description
prescription
[prescription comment]

example:
    钱 偏枯在左，血虚不萦筋骨，内风袭络，脉左缓大。（肝肾虚内风动。）
    制首乌（四两，烘） 枸杞子（去蒂，二两） 归身（二两，用独枝者，去梢） 怀牛膝（二两，蒸） 明天麻（二两，面煨） 三角胡麻（二两，打碎，水洗十次，烘） 黄甘菊（三两，水煎汁） 川石斛（四两，水煎汁） 小黑豆皮（四两，煎汁）
    用三汁膏加蜜，丸极细，早服四钱，滚水送。
	
    陈（四七） 肝血肾液内枯，阳扰风旋乘窍。大忌风药寒凉。
    炒杞子 桂圆肉 炒菊花 炙黑甘草 黄芪（去心） 牡蛎
*/
public class YiAnParser extends AbstractSingleLineParser {

	public YiAnParser() {
		super(new ArrayList<String>());
	}

	@Override
	public AbstractSingleLineParser parse(String line) {
		// System.out.println(line);
		if (line.startsWith("// comment")) {
			return this;
		}

		if (line.isEmpty()) {
			if (mCurrentYiAn != null) {
				if (mCurrentYiAn.details.size() == 0) {
					System.out.println("error " + line);
				}
			}
			mCurrentYiAn = null;
			return this;
		}

		if (line.startsWith("[comment]")) {
			return this;
		}

		if (mCurrentYiAn != null) {
			System.out.println("error " + line);
		}

		mCurrentYiAn = new YiAnEntity();
		mCurrentYiAn.details = new ArrayList<YiAnDetailEntity>();
		mYiAns.add(mCurrentYiAn);

		YiAnDetailParser parser = new YiAnDetailParser(this, mCurrentYiAn, mAdjustedTexts);
		return parser.parse(line);
	}

	//
	public void setParentSection(SectionEntity sectionEntity) {
		mParentSection = sectionEntity;
	}

	// find better way to save the yiAn and section to db. make the design clean
	public void save() {
		Repository r = new Repository();
		r.create(mYiAns);
	}

	public void adjust() {
		List<String> allPrescriptions = new ArrayList<String>();
		for (YiAnEntity entity : mYiAns) {
			for (YiAnDetailEntity detail : entity.details) {
				List<YiAnPrescriptionEntity> toBeDeleted = new ArrayList<YiAnPrescriptionEntity>();
				for (YiAnPrescriptionEntity prescription : detail.prescriptions) {
					if (prescription.summary == null){
						prescription.summary = "";
					}
					prescription.summary = StringUtils.strip(prescription.summary);
					
					if (prescription.summary.isEmpty()){
						toBeDeleted.add(prescription);
						continue;
					}
					
					allPrescriptions.add(prescription.summary);
				}
				for(YiAnPrescriptionEntity prescription : toBeDeleted){
					detail.prescriptions.remove(prescription);
				}
			}
		}

		PrescriptionAnalyzer a = new PrescriptionAnalyzer(allPrescriptions);
		for(String item : a.getBestOption()){
			System.out.print(item + " ");
		}
		
		System.out.println(" ****");
	}

	public void validate() {
		for (YiAnEntity entity : mYiAns) {
			if (entity.details.isEmpty()) {
				System.out.println("YiAn Detail is empty");
			}
			for (YiAnDetailEntity detail : entity.details) {
				if (detail.prescriptions.isEmpty()) {
					System.out.println("no prescription " + detail.content);
				}

				for (YiAnPrescriptionEntity prescription : detail.prescriptions) {
					if (prescription.items.isEmpty()) {
						System.out.println("no prescrption item: " + detail.content);
					}
					for (YiAnPrescriptionItemEntity item : prescription.items) {

					}
				}
			}
		}
	}

	protected List<String> mAdjustedTexts = new ArrayList<String>();
	private List<YiAnEntity> mYiAns = new ArrayList<YiAnEntity>();
	private YiAnEntity mCurrentYiAn;

	private SectionEntity mParentSection = null;
}
