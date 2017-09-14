package com.hubert.dataprovider;

import java.io.IOException;
import java.util.ArrayList;

//import com.hubert.dal.DbBuilder;
import com.hubert.dal.entity.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.YiAn.*;
import com.hubert.parser.tokenextractor.*;

public class importor {

	public static void main(String[] args) {
		try {
			// TODO Auto-generated method stub
			// DbBuilder builder = new DbBuilder();
			// builder.build();

			// gu_jin_yi_an_an_importer importor = new
			// gu_jin_yi_an_an_importer();
			// importor.doImport();

			testCase1ParseYiAn();
			testCase2ParseYiAn();
			testCase3ParseYiAn();
			testCase4ParseYiAn();
			testCase5ParseYiAn();
			testCase6ParseYiAn();
			// HerbAliasManager aliasManager = new HerbAliasManager();
			// aliasManager.load();
			// HerbAliasManager.getInstance().trace();

			testCase2();

			BookGenerator generator = new BookGenerator("resource/临证指南医案/format_ignore.txt", "临证指南医案");
			generator.doImport();

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

		new com.hubert.parser.LL1.Grammar(terminalSymbols, expressions);
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

		new com.hubert.parser.LL1.Grammar(terminalSymbols, expressions);
	}

	public static void testCase1ParseYiAn() throws Exception {
		// ***
		// 描述 阳挟内风上巅，目昏耳鸣不寐，肝经主病。
		// 处方 熟地（炙） 炙龟甲 萸肉 五味 磁石 茯苓 旱莲草 女贞子
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(TokenType.Description, "阳挟内风上巅，目昏耳鸣不寐，肝经主病。"));
		tokens.add(new Token(TokenType.LiteralText, "熟地（炙） 炙龟甲 萸肉 五味 磁石 茯苓 旱莲草 女贞子"));
		tokens.add(new Token(TokenType.End));

		com.hubert.parser.LL1.Grammar grammar = new com.hubert.parser.LL1.Grammar("resource/临证指南医案/format_ignore.txt");
		com.hubert.parser.LL1.YiAnParser parser = new com.hubert.parser.LL1.YiAnParser();
		ASTNode node = parser.parse(grammar, tokens);
		LogVisitor visitor = new LogVisitor("resource/debug/ASTNode_test1_ignore.json");
		node.accept(visitor);
		SectionEntity entity = new SectionEntity();
		entity.book = new BookEntity();
		entity.name = "testCase1ParseYiAn";
		entity.book.name = "debug";
		YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, HerbAliasManager.getInstance());
		node.accept(yiAnBuilder);
		System.out.println("TODO: write vistor to print the ASTNode");
	}

	public static void testCase2ParseYiAn() throws Exception {
		// ***
		// 描述 曾（五二）脉弦动，眩晕耳聋，行走气促无力，肛痔下垂。此未老欲衰，肾阴弱，收纳无权，肝阳炽，虚风蒙窍.......
		// 处方 [abbr]虎潜去锁阳、知母，加大肉苁蓉，炼蜜丸。
		// 处方 [format]黄柏 龟板 生地黄 陈皮 白芍 虎骨 干姜 肉苁蓉
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(TokenType.Description, "曾（五二）脉弦动，眩晕耳聋，行走气促无力，肛痔下垂。此未老欲衰，肾阴弱，收纳无权，肝阳炽，虚风蒙窍......."));
		tokens.add(new Token(TokenType.RecipeAbbreviation, "虎潜去锁阳、知母，加大肉苁蓉，炼蜜丸。"));
		tokens.add(new Token(TokenType.Herb, "黄柏"));
		tokens.add(new Token(TokenType.Herb, "龟板"));
		tokens.add(new Token(TokenType.Herb, "生地黄"));
		tokens.add(new Token(TokenType.Herb, "陈皮"));
		tokens.add(new Token(TokenType.End));

		com.hubert.parser.LL1.Grammar grammar = new com.hubert.parser.LL1.Grammar("resource/临证指南医案/format_ignore.txt");
		com.hubert.parser.LL1.YiAnParser parser = new com.hubert.parser.LL1.YiAnParser();
		ASTNode node = parser.parse(grammar, tokens);
		LogVisitor visitor = new LogVisitor("resource/debug/ASTNode_test2_ignore.json");
		node.accept(visitor);

		SectionEntity entity = new SectionEntity();
		entity.book = new BookEntity();
		entity.name = "testCase2ParseYiAn";
		entity.book.name = "debug";
		YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, HerbAliasManager.getInstance());
		node.accept(yiAnBuilder);
		System.out.println("TODO: write vistor to print the ASTNode");
	}

	public static void testCase3ParseYiAn() throws Exception {

		// ***
		// 描述 某（妪）今年风木司天，春夏阳升之候，兼因平昔怒劳忧思.....
		// 处方 金石斛（三钱） 化橘红（五分） 白蒺藜（二钱） 真北秦皮（一钱） 草决明（二钱） 冬桑叶（一钱） 嫩钩藤（一钱） 生白芍（一钱）
		// 次诊 又 前议苦辛酸降一法，肝风胃阳已折其上引之威，.....
		// 处方 [abbr]九制熟地 肉苁蓉（用大而黑色者，去甲切片，盛竹篮内，放长流水中浸七日，晒干，以极淡为度，四两）.....
		// 处方 [format]熟地 砂仁 肉苁蓉 虎骨 怀牛膝 制首乌 川萆薢 川石斛 赤茯苓 白茯苓 柏子霜
		// 处方说明 上药照方制末，另用小黑稆豆皮八两煎浓汁，法丸，每早百滚水服三钱。
		// 处方开头 [RH]议晚上用健中运痰，兼制亢阳。火动风生，从《外台》茯苓饮意。
		// 处方 人参（二两） 熟半夏（二两） 茯苓（四两，生） 广皮肉（二两） 川连（姜汁炒，一两） 枳实（麸炒，二两） 明天麻（二两，煨）
		// 处方说明 上末，用竹沥一杯，姜汁十匙，法丸，食远开水服三钱。
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(TokenType.Description, "某（妪）今年风木司天，春夏阳升之候，兼因平昔怒劳忧思....."));
		tokens.add(new Token(TokenType.RecipeAbbreviation, "虎潜去锁阳、知母，加大肉苁蓉，炼蜜丸。"));
		tokens.add(new Token(TokenType.Herb, "黄柏"));
		tokens.add(new Token(TokenType.Herb, "龟板"));
		tokens.add(new Token(TokenType.Herb, "生地黄"));
		tokens.add(new Token(TokenType.Herb, "陈皮"));
		tokens.add(new Token(TokenType.Description, "又 前议苦辛酸降一法，肝风胃阳已折其上引之威，....."));
		tokens.add(new Token(TokenType.RecipeAbbreviation, "九制熟地 肉苁蓉（用大而黑色者，去甲切片，盛竹篮内，放长流水中浸七日，晒干，以极淡为度，四两）....."));
		tokens.add(new Token(TokenType.Herb, "黄柏"));
		tokens.add(new Token(TokenType.Herb, "龟板"));
		tokens.add(new Token(TokenType.Herb, "生地黄"));
		tokens.add(new Token(TokenType.Herb, "陈皮"));
		tokens.add(new Token(TokenType.LiteralText, "上药照方制末，另用小黑稆豆皮八两煎浓汁，法丸，每早百滚水服三钱。"));
		tokens.add(new Token(TokenType.RecipeHeader, "议晚上用健中运痰，兼制亢阳。火动风生，从《外台》茯苓饮意。"));
		tokens.add(new Token(TokenType.LiteralText, "人参（二两） 熟半夏（二两） 茯苓（四两，生） 广皮肉（二两） 川连（姜汁炒，一两） 枳实（麸炒，二两） 明天麻（二两，煨）"));
		tokens.add(new Token(TokenType.LiteralText, "上末，用竹沥一杯，姜汁十匙，法丸，食远开水服三钱。"));

		tokens.add(new Token(TokenType.End));

		com.hubert.parser.LL1.Grammar grammar = new com.hubert.parser.LL1.Grammar("resource/临证指南医案/format_ignore.txt");
		com.hubert.parser.LL1.YiAnParser parser = new com.hubert.parser.LL1.YiAnParser();
		ASTNode node = parser.parse(grammar, tokens);
		LogVisitor visitor = new LogVisitor("resource/debug/ASTNode_test3_ignore.json");
		node.accept(visitor);

		SectionEntity entity = new SectionEntity();
		entity.book = new BookEntity();
		entity.book.name = "testCase3ParseYiAn";
		YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, HerbAliasManager.getInstance());
		node.accept(yiAnBuilder);
		System.out.println("TODO: write vistor to print the ASTNode");
	}

	public static void testCase4ParseYiAn() throws Exception {

		// ***
		// 描述 叶 初春肝风内动，眩晕跌仆，左肢偏痿，舌络不和，呼吸不爽，痰火上蒙，根本下衰。先宜清上痰火。
		// 处方 羚羊角 茯苓 橘红 桂枝 半夏 郁金 竹沥 姜汁
		// 次诊 又 风热烁筋骨为痛，痰火气阻，呼吸不利。照前方去郁金、竹沥、姜汁，加白蒺藜，钩藤。
		// 处方 [format]羚羊角 茯苓 橘红 桂枝 半夏 白蒺藜 钩藤
		// 处方 [format]又 炒半夏 茯苓 钩藤 橘红 金石斛 石菖蒲 竹沥 姜汁
		// 处方 [format]又 人参 半夏 枳实 茯苓 橘红 蒺藜 竹沥 姜汁
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(TokenType.Description, "叶 初春肝风内动，眩晕跌仆，左肢偏痿...."));
		tokens.add(new Token(TokenType.Herb, "黄柏"));
		tokens.add(new Token(TokenType.Herb, "龟板"));
		tokens.add(new Token(TokenType.Herb, "生地黄"));
		tokens.add(new Token(TokenType.Herb, "陈皮"));
		tokens.add(new Token(TokenType.Description, "又 风热烁筋骨为痛，痰火气阻，呼吸不利，....."));
		tokens.add(new Token(TokenType.Herb, "人参"));
		tokens.add(new Token(TokenType.Herb, "半夏"));
		tokens.add(new Token(TokenType.Herb, "枳实"));
		tokens.add(new Token(TokenType.Herb, "茯苓"));
		tokens.add(new Token(TokenType.Description, "又"));
		tokens.add(new Token(TokenType.Herb, "黄柏"));
		tokens.add(new Token(TokenType.Herb, "龟板"));
		tokens.add(new Token(TokenType.Herb, "生地黄"));
		tokens.add(new Token(TokenType.Herb, "陈皮"));

		tokens.add(new Token(TokenType.End));

		com.hubert.parser.LL1.Grammar grammar = new com.hubert.parser.LL1.Grammar("resource/临证指南医案/format_ignore.txt");
		com.hubert.parser.LL1.YiAnParser parser = new com.hubert.parser.LL1.YiAnParser();
		ASTNode node = parser.parse(grammar, tokens);
		LogVisitor visitor = new LogVisitor("resource/debug/ASTNode_test4_ignore.json");
		node.accept(visitor);

		SectionEntity entity = new SectionEntity();
		entity.book = new BookEntity();
		entity.name = "testCase4ParseYiAn";
		entity.book.name = "debug";
		YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, HerbAliasManager.getInstance());
		node.accept(yiAnBuilder);
		System.out.println("TODO: write vistor to print the ASTNode");
	}

	public static void testCase5ParseYiAn() throws Exception {
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(TokenType.Description, "阳挟内风上巅，目昏耳鸣不寐，肝经主病。"));
		tokens.add(new Token(TokenType.LiteralText, "熟地（炙）"));
		tokens.add(new Token(TokenType.End));
		tokens.add(new Token(TokenType.Description, "曾（五二）脉弦动，眩晕耳聋，行走气促无力，肛痔下垂。此未老欲衰，肾阴弱，收纳无权，肝阳炽，虚风蒙窍......."));
		tokens.add(new Token(TokenType.Herb, "黄柏"));
		tokens.add(new Token(TokenType.Herb, "龟板"));
		tokens.add(new Token(TokenType.End));

		com.hubert.parser.LL1.Grammar grammar = new com.hubert.parser.LL1.Grammar("resource/临证指南医案/format_ignore.txt");
		com.hubert.parser.LL1.YiAnParser parser = new com.hubert.parser.LL1.YiAnParser();
		ASTNode node = parser.parse(grammar, tokens);
		LogVisitor visitor = new LogVisitor("resource/debug/ASTNode_test5_ignore.json");
		node.accept(visitor);

		SectionEntity entity = new SectionEntity();
		entity.book = new BookEntity();
		entity.name = "testCase5ParseYiAn";
		entity.book.name = "debug";
		YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, HerbAliasManager.getInstance());
		node.accept(yiAnBuilder);
		System.out.println("TODO: write vistor to print the ASTNode");
	}

	public static void testCase6ParseYiAn() throws Exception {
		// 又 今年天符岁会，上半年阳气大泄，.....
		// 人参（一钱） 生牡蛎（五钱） 生白芍（二钱）
		// 上午服。
		// （丸方） 人参（二两） 茯苓（三两，生）
		// 为末，竹沥法丸，早上服三钱，百滚汤下。

		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token(TokenType.Description, "又 今年天符岁会，上半年阳气大泄，....."));
		tokens.add(new Token(TokenType.LiteralText, "人参（一钱） 生牡蛎（五钱） 生白芍（二钱"));
		tokens.add(new Token(TokenType.LiteralText, "上午服。"));
		tokens.add(new Token(TokenType.Herb, "人参"));
		tokens.add(new Token(TokenType.Herb, "茯苓"));
		tokens.add(new Token(TokenType.LiteralText, "为末，竹沥法丸，早上服三钱，百滚汤下。"));
		tokens.add(new Token(TokenType.End));

		com.hubert.parser.LL1.Grammar grammar = new com.hubert.parser.LL1.Grammar("resource/临证指南医案/format_ignore.txt");
		com.hubert.parser.LL1.YiAnParser parser = new com.hubert.parser.LL1.YiAnParser();
		ASTNode node = parser.parse(grammar, tokens);
		LogVisitor visitor = new LogVisitor("resource/debug/ASTNode_test6_ignore.json");
		node.accept(visitor);

		SectionEntity entity = new SectionEntity();
		entity.name = "testCase6ParseYiAn";
		entity.book = new BookEntity();
		entity.book.name = "debug";
		YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, HerbAliasManager.getInstance());
		node.accept(yiAnBuilder);
		System.out.println("TODO: write vistor to print the ASTNode");
	}

}
