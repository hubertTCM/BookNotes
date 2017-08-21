package com.hubert.dataprovider;

import com.hubert.dal.DbBuilder;

public class importor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DbBuilder builder = new DbBuilder();
		builder.build();

		//gu_jin_yi_an_an_importer importor = new gu_jin_yi_an_an_importer();
		//importor.doImport();
		
		BookGenerator generator = new BookGenerator("临证指南医案");
		generator.doImport();
		
		//HerbAliasManager  aliasManager  = new HerbAliasManager();
		//aliasManager.load();
		//HerbAliasManager.getInstance().trace();
		//System.out.println("done");
	}

}
