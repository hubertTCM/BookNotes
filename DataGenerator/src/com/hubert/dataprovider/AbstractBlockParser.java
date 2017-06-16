package com.hubert.dataprovider;

import com.hubert.dal.entity.*;

public abstract class AbstractBlockParser {
	public void SetSection(SectionEntity section) {
		mParentSection = section;
	}

	public abstract void parse(String line) ;

	public abstract void save() ;

	protected SectionEntity mParentSection;
}
