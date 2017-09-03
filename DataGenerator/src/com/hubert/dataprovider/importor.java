package com.hubert.dataprovider;

import java.io.IOException;
import java.util.ArrayList;

import com.hubert.dal.DbBuilder;

public class importor {

	public static void main(String[] args) {
		try {
			// TODO Auto-generated method stub
			// DbBuilder builder = new DbBuilder();
			// builder.build();

			// gu_jin_yi_an_an_importer importor = new
			// gu_jin_yi_an_an_importer();
			// importor.doImport();
			// com.hubert.dataprovider.parser.LL1.YiAnParser parser = new
			// com.hubert.dataprovider.parser.LL1.YiAnParser(
			// "resource/临证指南医案/format_ignore.txt");
			// S –> AB
			// A –> Ca | ε
			// B –> cB'
			// B' –> aACB' | ε
			// C –> b | ε

			ArrayList<String> expressions = new ArrayList<String>();
			expressions.add("S := A B");
			expressions.add("A := C a");
			expressions.add("A := Empty");
			expressions.add("B := c B'");
			expressions.add("B' := a A C B'");
			expressions.add("B' := Empty");
			expressions.add("C := b");
			expressions.add("C := Empty");
			ArrayList<String> terminalSymbols = new ArrayList<String>();
			terminalSymbols.add("a");
			terminalSymbols.add("c");
			terminalSymbols.add("b");

			com.hubert.dataprovider.parser.LL1.YiAnParser parser2 = new com.hubert.dataprovider.parser.LL1.YiAnParser(
					terminalSymbols, expressions);
			parser2.parse(null);
			// 计算结果如下：
			//
			// First(C) = {b, ε}
			// First(B') = {a, ε}
			// First(B) = {c}
			// First(A) = {b, a, ε}
			// First(S) = {b, a, c}
			//
			// Follow(S) = {$}
			// Follow(B) = {$}
			// Follow(B') = {$}
			// Follow(C) = {a, $}
			// Follow(A) = {c, b, a, $}

			// BookGenerator generator = new BookGenerator("临证指南医案");
			// generator.doImport();

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
