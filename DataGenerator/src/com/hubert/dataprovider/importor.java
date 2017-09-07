package com.hubert.dataprovider;

import java.io.IOException;
import java.util.ArrayList;

import com.hubert.dal.DbBuilder;
import com.hubert.dataprovider.parser.tokenextractor.*;

public class importor {

	public static void main(String[] args) {
		try {
			// TODO Auto-generated method stub
			// DbBuilder builder = new DbBuilder();
			// builder.build();

			// gu_jin_yi_an_an_importer importor = new
			// gu_jin_yi_an_an_importer();
			// importor.doImport();
			
			
			
			BookGenerator generator = new BookGenerator("临证指南医案");
			generator.doImport();
			

			testCase1ParseYiAn();
			// HerbAliasManager aliasManager = new HerbAliasManager();
			// aliasManager.load();
			// HerbAliasManager.getInstance().trace();

			testCase2();

			System.out.println("done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testCase1() throws Exception {

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

		com.hubert.dataprovider.parser.LL1.Grammar parser2 = new com.hubert.dataprovider.parser.LL1.Grammar(
				terminalSymbols, expressions);
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
	}

	public static void testCase2() throws Exception {
		// S -> TE'
		// E' -> +TE' | ε
		// T -> FT'
		// T' -> *FT' | ε
		// F -> (S) | id

		ArrayList<String> expressions = new ArrayList<String>();
		expressions.add("S  := T E'");
		expressions.add("E' := + T E'");
		expressions.add("E'  := Empty");
		expressions.add("T  := F T'");
		expressions.add("T'  := * F T'");
		expressions.add("T'  := Empty");
		expressions.add("F  := ( S )");
		expressions.add("F  := id");
		ArrayList<String> terminalSymbols = new ArrayList<String>();
		terminalSymbols.add("(");
		terminalSymbols.add(")");
		terminalSymbols.add("id");
		terminalSymbols.add("+");
		terminalSymbols.add("*");
		terminalSymbols.add("Empty");

		com.hubert.dataprovider.parser.LL1.Grammar parser2 = new com.hubert.dataprovider.parser.LL1.Grammar(
				terminalSymbols, expressions);
	}
	
	public static void testCase1ParseYiAn() throws Exception{
		
		// ***
		// 描述 阳挟内风上巅，目昏耳鸣不寐，肝经主病。
		// 处方 熟地（炙） 炙龟甲 萸肉 五味 磁石 茯苓 旱莲草 女贞子
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(TokenType.Description, "阳挟内风上巅，目昏耳鸣不寐，肝经主病。"));
		tokens.add(new Token(TokenType.LiteralText, "熟地（炙） 炙龟甲 萸肉 五味 磁石 茯苓 旱莲草 女贞子"));
		tokens.add(new Token(TokenType.End));

		com.hubert.dataprovider.parser.LL1.Grammar grammar = new com.hubert.dataprovider.parser.LL1.Grammar(
				"resource/临证指南医案/format_ignore.txt");
		com.hubert.dataprovider.parser.LL1.YiAnParser parser = new com.hubert.dataprovider.parser.LL1.YiAnParser();
		parser.parse(grammar, tokens);
		System.out.println("TODO: write vistor to print the ASTNode");
	}
}
