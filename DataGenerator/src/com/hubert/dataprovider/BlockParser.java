package com.hubert.dataprovider;

import com.hubert.dal.entity.*;

public class BlockParser {
	public void SetSection(SectionEntity section){
		mParentSection = section;
	}
	
	public void parse(String line){
	}

	protected SectionEntity mParentSection;
}
