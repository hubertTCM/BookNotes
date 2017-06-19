package com.hubert.dataprovider;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;

public class YiAnPrescriptionParser extends AbstractSingleLineParser {

	public YiAnPrescriptionParser(YiAnDetailParser yianDetailParser,YiAnDetailEntity yianDetailEntity, List<String> adjustedTexts) {
		super(adjustedTexts);
		mPrescriptionItemParser = new PrescriptionItemsParser();
		
		mYiAnDetailParser = yianDetailParser;
		mYiAnDetailEntity = yianDetailEntity;

		createYiAnPrescriptionEntity();
	}

	private void createYiAnPrescriptionEntity() {
		mYiAnPrescriptionEntity = new YiAnPrescriptionEntity();
		mYiAnPrescriptionEntity.items = new ArrayList<YiAnPrescriptionItemEntity>();
	}

	@Override
	public AbstractSingleLineParser parse(String line) {
		if (line.isEmpty() || YiAnDetailParser.isNextYiAnDetail(line)) {
			mYiAnDetailEntity.prescriptions.add(mYiAnPrescriptionEntity);
			return mYiAnDetailParser.parse(line);
		}

		// [abbr]复脉汤去姜、桂。
		if (line.startsWith("[abbr]")){
			return this;
		}
		String formatPrefix = "[format]";
		if (line.startsWith(formatPrefix)){
			line = line.substring(formatPrefix.length(), line.length());
		}
//		
//		又 前议苦辛酸降一法，肝风胃阳已折其上引之威，是诸症亦觉小愈，虽曰治标，正合岁气节候而设。思夏至一阴来复，高年本病，预宜持护，自来中厥，最防于暴寒骤加，致身中阴阳两不接续耳。议得摄纳肝肾真气，补益下虚本病。
//	    九制熟地（先用水煮半日，徐加醇酒、砂仁，再煮一日，晒干再蒸，如法九次，干者炒存性，八两） 肉苁蓉（用大而黑色者，去甲切片，盛竹篮内，放长流水中浸七日，晒干，以极淡为度，四两） 生虎膝骨（另捣碎，研，二两） 怀牛膝（盐水蒸，三两） 制首乌（四两，烘） 川萆薢（盐水炒，二两） 川石斛（八两，熬膏） 赤白茯苓（四两） 柏子霜（二两）
//	    上药照方制末，另用小黑稆豆皮八两煎浓汁，法丸，每早百滚水服三钱。
//	  [RH]议晚上用健中运痰，兼制亢阳。火动风生，从《外台》茯苓饮意。
//	    人参（二两） 熟半夏（二两） 茯苓（四两，生） 广皮肉（二两） 川连（姜汁炒，一两） 枳实（麸炒，二两） 明天麻（二两，煨） 钩藤（三两） 白蒺藜（鸡子黄拌煮，洗净炒，去刺，三两）  地栗粉（二两）
//	    上末，用竹沥一杯，姜汁十匙，法丸，食远开水服三钱。

		
		String recipeHeaderPrefix = "[RH]";
		if (line.startsWith(recipeHeaderPrefix)){
			mYiAnDetailEntity.prescriptions.add(mYiAnPrescriptionEntity);
			createYiAnPrescriptionEntity();
			return this;
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
	private YiAnDetailEntity mYiAnDetailEntity;
}