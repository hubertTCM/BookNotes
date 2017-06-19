package com.hubert.dataprovider;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;

/* 
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
		if (line.isEmpty() || line.startsWith("// comment")) {
			return this;
		}

		if (mCurrentYiAn != null) {
			mYiAns.add(mCurrentYiAn);
		}

		if (line.startsWith("[comment]")) {
			return this;
		}

		mCurrentYiAn = new YiAnEntity();
		mCurrentYiAn.details = new ArrayList<YiAnDetailEntity>();

		YiAnDetailParser parser = new YiAnDetailParser(this, mCurrentYiAn, mAdjustedTexts);
		// parser.parse(line);

		return parser.parse(line);
	}

	// @Override
	public void save() {
		// TODO Auto-generated method stub

	}

	protected List<String> mAdjustedTexts = new ArrayList<String>();
	private List<YiAnEntity> mYiAns = new ArrayList<YiAnEntity>();
	private YiAnEntity mCurrentYiAn;
	// private YiAnDetailParser mYiAnDetailParser;
}
