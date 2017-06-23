package com.hubert.dataprovider;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;


// 制首乌（四两，烘） 枸杞子（去蒂，二两） 归身（二两，用独枝者，去梢） 怀牛膝（二两，蒸） 明天麻（二两，面煨） 三角胡麻（二两，打碎，水洗十次，烘） 黄甘菊（三两，水煎汁） 川石斛（四两，水煎汁） 小黑豆皮（四两，煎汁）
// 生地 阿胶 牡蛎 川斛 知母
public class PrescriptionItemsParser {
	public Collection<PrescriptionItemEntity> parse(String text) {
		Collection<PrescriptionItemEntity> items = new ArrayList<PrescriptionItemEntity>();
		// https://stackoverflow.com/questions/7899525/how-to-split-a-string-by-space
		String[] compositions = text.split("\\s+");
		for (String part : compositions) {
			PrescriptionItemEntity entity = createPrescriptionEntity(part);
			if (entity != null) {
				items.add(entity);
			}
		}
		return items;
	}

	private PrescriptionItemEntity createPrescriptionEntity(String text) {
		text = StringUtils.strip(text);
		if (text.isEmpty()) {
			return null;
		}

		PrescriptionItemEntity entity = new PrescriptionItemEntity();
		int index = text.indexOf("（");
		if (index < 0){
			index = text.indexOf("(");
		}
		
		if (index < 0) {
			entity.herb = text;
		} else {
			entity.herb = text.substring(0, index);
		}
		
		if (entity.herb == null){
			entity.herb = "";
		}
		entity.herb = StringUtils.strip(entity.herb);
		if (entity.herb.isEmpty()){
			return null;
		}
		//System.out.println(entity.herb);
		return entity;
	}
}
