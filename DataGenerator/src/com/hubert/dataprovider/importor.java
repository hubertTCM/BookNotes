package com.hubert.dataprovider;

import java.io.IOException;

import com.hubert.dal.DbBuilder;

public class importor {

	public static void main(String[] args) {
		try {
			// TODO Auto-generated method stub
			DbBuilder builder = new DbBuilder();
			builder.build();

			// gu_jin_yi_an_an_importer importor = new
			// gu_jin_yi_an_an_importer();
			// importor.doImport();
			com.hubert.dataprovider.parser.LL1.YiAnParser parser = new com.hubert.dataprovider.parser.LL1.YiAnParser(
					"resource/临证指南医案/format_ignore.txt");

			BookGenerator generator = new BookGenerator("临证指南医案");
			generator.doImport();

			// HerbAliasManager aliasManager = new HerbAliasManager();
			// aliasManager.load();
			// HerbAliasManager.getInstance().trace();
			System.out.println("done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
