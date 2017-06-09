package com.hubert.dataprovider;

import com.hubert.dal.DbBuilder;

public class importor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DbBuilder builder = new DbBuilder();
		builder.build();

		gu_jin_yi_an_an_impoter importor = new gu_jin_yi_an_an_impoter();
		importor.doImport();
		
		System.out.println("done");
	}

}
