package com.hubert;

import static org.junit.Assert.*;

import java.util.*;
//import java.util.stream.*; //it is too slow to use lambda...

import org.apache.commons.collections4.*;
import org.junit.*;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.YiAn.*;
import com.hubert.parser.LL1.*;
import com.hubert.parser.tokenextractor.YiAn.*;

public class YiAnParserTests {
    @BeforeClass
    public static void init() {
        mGrammarFile = "../DataGenerator/resource/临证指南医案/grammar.xml";
        mHerbAliasManager = HerbAliasManager.getInstance("../DataGenerator/resource/常用中药处方别名.txt");
        mUtils = new Utils();
    }

    @Test
    public void test1() {
        try {
            // ***
            // 描述 阳挟内风上巅，目昏耳鸣不寐，肝经主病。
            // 处方 熟地（炙） 炙龟甲 萸肉 五味 磁石 茯苓 旱莲草 女贞子
            ArrayList<Token> tokens = new ArrayList<Token>();
            String description = "阳挟内风上巅，目昏耳鸣不寐，肝经主病。";
            tokens.add(new Token(TokenType.Description, "阳挟内风上巅，目昏耳鸣不寐，肝经主病。"));
            String recipe = "熟地（炙） 炙龟甲 萸肉 五味 磁石 茯苓 旱莲草 女贞子";
            tokens.add(new Token(TokenType.LiteralText, recipe));
            tokens.add(new Token(TokenType.End));

            Grammar grammar = new Grammar(mGrammarFile);
            YiAnParser parser = new YiAnParser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test1.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.book = new BookEntity();
            entity.name = "test1";
            entity.book.name = "debug";
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager);
            node.accept(yiAnBuilder);

            List<YiAnEntity> yiAns = yiAnBuilder.getYiAns();
            assertEquals("only one YiAn", 1, yiAns.size());
            YiAnEntity yiAn = yiAns.get(0);
            assertEquals("only one YiAn Detail", 1, yiAn.details.size());
            YiAnDetailEntity detail = (YiAnDetailEntity) yiAn.details.toArray()[0];
            // Token description = tokens.stream().filter(x -> x.getType() ==
            // TokenType.Description).findFirst().get();
            assertEquals("check description", description, detail.content);

            // Token recipe = tokens.stream().filter(x -> x.getType() ==
            // TokenType.LiteralText).findFirst().get();
            assertEquals("check prescriptions", 1, detail.prescriptions.size());
            checkHerbs(recipe, detail.prescriptions.iterator().next().items);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void test2() {
        try {
            // ***
            // 描述 曾（五二）脉弦动，眩晕耳聋，行走气促无力，肛痔下垂。此未老欲衰，肾阴弱，收纳无权，肝阳炽，虚风蒙窍.......
            // 处方 [abbr]虎潜去锁阳、知母，加大肉苁蓉，炼蜜丸。
            // 处方 [format]黄柏 龟板 生地黄 陈皮 白芍 虎骨 干姜 肉苁蓉
            ArrayList<Token> tokens = new ArrayList<Token>();
            String description = "曾（五二）脉弦动，眩晕耳聋，行走气促无力，肛痔下垂。此未老欲衰，肾阴弱，收纳无权，肝阳炽，虚风蒙窍.......";
            tokens.add(new Token(TokenType.Description, description));
            tokens.add(new Token(TokenType.RecipeAbbreviation, "虎潜去锁阳、知母，加大肉苁蓉，炼蜜丸。"));
            ArrayList<Token> herbs = new ArrayList<Token>();
            herbs.add(new Token(TokenType.Herb, "黄柏"));
            herbs.add(new Token(TokenType.Herb, "龟板"));
            herbs.add(new Token(TokenType.Herb, "生地黄"));
            herbs.add(new Token(TokenType.Herb, "陈皮"));
            tokens.addAll(herbs);
            tokens.add(new Token(TokenType.End));

            Grammar grammar = new Grammar(mGrammarFile);
            YiAnParser parser = new YiAnParser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test2.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.book = new BookEntity();
            entity.name = "test2";
            entity.book.name = "debug";
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager);
            node.accept(yiAnBuilder);

            List<YiAnEntity> yiAns = yiAnBuilder.getYiAns();
            assertEquals("only one YiAn", 1, yiAns.size());
            YiAnEntity yiAn = yiAns.get(0);
            assertEquals("only one YiAn Detail", 1, yiAn.details.size());
            YiAnDetailEntity detail = (YiAnDetailEntity) yiAn.details.toArray()[0];
            // Token description = tokens.stream().filter(x -> x.getType() ==
            // TokenType.Description).findFirst().get();
            assertEquals("check description", description, detail.content);

            // List<Token> herbs =
            // tokens.stream().filter(x->TokenType.Herb.equals(x.getType())).collect(Collectors.toList());
            checkHerbs(herbs, detail.prescriptions.iterator().next().items);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void test3() {
        try {

            // ***
            // 描述 某（妪）今年风木司天，春夏阳升之候，兼因平昔怒劳忧思.....
            // 处方 金石斛（三钱） 化橘红（五分） 白蒺藜（二钱） 真北秦皮（一钱） 草决明（二钱） 冬桑叶（一钱） 嫩钩藤（一钱）
            // 生白芍（一钱）
            // 次诊 又 前议苦辛酸降一法，肝风胃阳已折其上引之威，.....
            // 处方 [abbr]九制熟地 肉苁蓉（用大而黑色者，去甲切片，盛竹篮内，放长流水中浸七日，晒干，以极淡为度，四两）.....
            // 处方 [format]熟地 砂仁 肉苁蓉 虎骨 怀牛膝 制首乌 川萆薢 川石斛 赤茯苓 白茯苓 柏子霜
            // 处方说明 上药照方制末，另用小黑稆豆皮八两煎浓汁，法丸，每早百滚水服三钱。
            // 处方开头 [RH]议晚上用健中运痰，兼制亢阳。火动风生，从《外台》茯苓饮意。
            // 处方 人参（二两） 熟半夏（二两） 茯苓（四两，生） 广皮肉（二两） 川连（姜汁炒，一两） 枳实（麸炒，二两） 明天麻（二两，煨）
            // 处方说明 上末，用竹沥一杯，姜汁十匙，法丸，食远开水服三钱。
            ArrayList<Token> tokens = new ArrayList<Token>();
            String description1 = "某（妪）今年风木司天，春夏阳升之候，兼因平昔怒劳忧思.....";
            tokens.add(new Token(TokenType.Description, description1));
            tokens.add(new Token(TokenType.RecipeAbbreviation, "虎潜去锁阳、知母，加大肉苁蓉，炼蜜丸。"));

            ArrayList<Token> herbs1 = new ArrayList<Token>();
            herbs1.add(new Token(TokenType.Herb, "黄柏"));
            herbs1.add(new Token(TokenType.Herb, "龟板"));
            herbs1.add(new Token(TokenType.Herb, "生地黄"));
            herbs1.add(new Token(TokenType.Herb, "陈皮"));
            tokens.addAll(herbs1);

            tokens.add(new Token(TokenType.Description, "又 前议苦辛酸降一法，肝风胃阳已折其上引之威，....."));
            tokens.add(new Token(TokenType.RecipeAbbreviation, "九制熟地 肉苁蓉（用大而黑色者，去甲切片，盛竹篮内，放长流水中浸七日，晒干，以极淡为度，四两）....."));

            ArrayList<Token> herbs2 = new ArrayList<Token>();
            herbs2.add(new Token(TokenType.Herb, "黄柏"));
            herbs2.add(new Token(TokenType.Herb, "龟板"));
            herbs2.add(new Token(TokenType.Herb, "生地黄"));
            herbs2.add(new Token(TokenType.Herb, "陈皮"));
            herbs2.add(new Token(TokenType.Herb, "肉苁蓉"));
            tokens.addAll(herbs2);

            tokens.add(new Token(TokenType.LiteralText, "上药照方制末，另用小黑稆豆皮八两煎浓汁，法丸，每早百滚水服三钱。"));
            tokens.add(new Token(TokenType.RecipeHeader, "议晚上用健中运痰，兼制亢阳。火动风生，从《外台》茯苓饮意。"));
            String recipe2 = "人参（二两） 熟半夏（二两） 茯苓（四两，生） 广皮肉（二两） 川连（姜汁炒，一两） 枳实（麸炒，二两） 明天麻（二两，煨）";
            tokens.add(new Token(TokenType.LiteralText, recipe2));
            tokens.add(new Token(TokenType.LiteralText, "上末，用竹沥一杯，姜汁十匙，法丸，食远开水服三钱。"));

            tokens.add(new Token(TokenType.End));

            Grammar grammar = new Grammar(mGrammarFile);
            YiAnParser parser = new YiAnParser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test3.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.book = new BookEntity();
            entity.name = "test3";
            entity.book.name = "debug";
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager);
            node.accept(yiAnBuilder);

            List<YiAnEntity> yiAns = yiAnBuilder.getYiAns();
            assertEquals("only one YiAn", 1, yiAns.size());
            YiAnEntity yiAn = yiAns.get(0);
            assertEquals("only two YiAn Detail", 2, yiAn.details.size());
            YiAnDetailEntity detail1 = (YiAnDetailEntity) yiAn.details.toArray()[0];
            assertEquals("check description", description1, detail1.content);

            assertEquals("check prescriptions", 1, detail1.prescriptions.size());
            checkHerbs(herbs1, detail1.prescriptions.iterator().next().items);

            YiAnDetailEntity detail2 = (YiAnDetailEntity) yiAn.details.toArray()[1];
            assertEquals("check prescriptions", 2, detail2.prescriptions.size());
            checkHerbs(herbs2, IterableUtils.get(detail2.prescriptions, 0).items);
            checkHerbs(recipe2, IterableUtils.get(detail2.prescriptions, 1).items);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void test4() {
        try {
            // ***
            // 描述 叶 初春肝风内动，眩晕跌仆，左肢偏痿，舌络不和，呼吸不爽，痰火上蒙，根本下衰。先宜清上痰火。
            // 处方 羚羊角 茯苓 橘红 桂枝 半夏 郁金 竹沥 姜汁
            // 次诊 又 风热烁筋骨为痛，痰火气阻，呼吸不利。照前方去郁金、竹沥、姜汁，加白蒺藜，钩藤。
            // 处方 [format]羚羊角 茯苓 橘红 桂枝 半夏 白蒺藜 钩藤
            // 处方 [format]又 炒半夏 茯苓 钩藤 橘红 金石斛 石菖蒲 竹沥 姜汁
            // 处方 [format]又 人参 半夏 枳实 茯苓 橘红 蒺藜 竹沥 姜汁
            ArrayList<Token> tokens = new ArrayList<Token>();
            String description1 = "叶 初春肝风内动，眩晕跌仆，左肢偏痿....";
            tokens.add(new Token(TokenType.Description, description1));
            ArrayList<Token> herbs1 = new ArrayList<Token>();
            herbs1.add(new Token(TokenType.Herb, "黄柏"));
            herbs1.add(new Token(TokenType.Herb, "龟板"));
            herbs1.add(new Token(TokenType.Herb, "生地黄"));
            herbs1.add(new Token(TokenType.Herb, "陈皮"));
            herbs1.add(new Token(TokenType.Herb, "茯苓"));
            tokens.addAll(herbs1);

            String description2 = "又 风热烁筋骨为痛，痰火气阻，呼吸不利，.....";
            tokens.add(new Token(TokenType.Description, description2));
            ArrayList<Token> herbs2 = new ArrayList<Token>();
            herbs2.add(new Token(TokenType.Herb, "人参"));
            herbs2.add(new Token(TokenType.Herb, "半夏"));
            herbs2.add(new Token(TokenType.Herb, "枳实"));
            herbs2.add(new Token(TokenType.Herb, "茯苓"));
            tokens.addAll(herbs2);

            tokens.add(new Token(TokenType.Description, "又"));
            ArrayList<Token> herbs3 = new ArrayList<Token>();
            herbs3.add(new Token(TokenType.Herb, "黄柏"));
            herbs3.add(new Token(TokenType.Herb, "龟板"));
            herbs3.add(new Token(TokenType.Herb, "生地黄"));
            herbs3.add(new Token(TokenType.Herb, "陈皮"));
            tokens.addAll(herbs3);

            tokens.add(new Token(TokenType.End));

            Grammar grammar = new Grammar(mGrammarFile);
            YiAnParser parser = new YiAnParser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test4.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.book = new BookEntity();
            entity.name = "test4";
            entity.book.name = "debug";
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager);
            node.accept(yiAnBuilder);

            List<YiAnEntity> yiAns = yiAnBuilder.getYiAns();
            assertEquals("only one YiAn", 1, yiAns.size());
            YiAnEntity yiAn = yiAns.get(0);
            assertEquals("only two YiAn Detail", 3, yiAn.details.size());
            YiAnDetailEntity detail1 = IterableUtils.get(yiAn.details, 0);// (YiAnDetailEntity)
                                                                          // yiAn.details.toArray()[0];
            assertEquals("check description", description1, detail1.content);

            assertEquals("check prescriptions", 1, detail1.prescriptions.size());
            checkHerbs(herbs1, detail1.prescriptions.iterator().next().items);

            YiAnDetailEntity detail2 = IterableUtils.get(yiAn.details, 1);
            assertEquals("check prescriptions", 1, detail2.prescriptions.size());
            checkHerbs(herbs2, IterableUtils.get(detail2.prescriptions, 0).items);

            YiAnDetailEntity detail3 = IterableUtils.get(yiAn.details, 2);
            assertEquals("check prescriptions", 1, detail3.prescriptions.size());
            checkHerbs(herbs3, IterableUtils.get(detail3.prescriptions, 0).items);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void test5() {
        try {

            ArrayList<Token> tokens = new ArrayList<Token>();
            String description1 = "阳挟内风上巅，目昏耳鸣不寐，肝经主病。";
            tokens.add(new Token(TokenType.Description, description1));
            ArrayList<Token> herbs1 = new ArrayList<Token>();
            herbs1.add(new Token(TokenType.LiteralText, "熟地（炙）"));
            tokens.addAll(herbs1);
            tokens.add(new Token(TokenType.End));

            String description2 = "曾（五二）脉弦动，眩晕耳聋，行走气促无力，肛痔下垂。此未老欲衰，肾阴弱，收纳无权，肝阳炽，虚风蒙窍.......";
            tokens.add(new Token(TokenType.Description, description2));
            ArrayList<Token> herbs2 = new ArrayList<Token>();
            herbs2.add(new Token(TokenType.Herb, "黄柏"));
            herbs2.add(new Token(TokenType.Herb, "龟板"));
            tokens.addAll(herbs2);
            tokens.add(new Token(TokenType.End));

            Grammar grammar = new Grammar(mGrammarFile);
            YiAnParser parser = new YiAnParser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test5.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.book = new BookEntity();
            entity.name = "test5";
            entity.book.name = "debug";
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager);
            node.accept(yiAnBuilder);

            List<YiAnEntity> yiAns = yiAnBuilder.getYiAns();
            assertEquals("only 2 YiAn", 2, yiAns.size());
            YiAnEntity yiAn1 = yiAns.get(0);
            assertEquals("only 1 YiAn Detail", 1, yiAn1.details.size());
            YiAnDetailEntity detail1 = IterableUtils.get(yiAn1.details, 0);// (YiAnDetailEntity)
                                                                           // yiAn.details.toArray()[0];
            assertEquals("check description", description1, detail1.content);
            checkHerbs(herbs1, IterableUtils.get(detail1.prescriptions, 0).items);

            YiAnEntity yiAn2 = yiAns.get(1);
            assertEquals("only 1 YiAn Detail", 1, yiAn1.details.size());
            YiAnDetailEntity detail2 = IterableUtils.get(yiAn2.details, 0);// (YiAnDetailEntity)
                                                                           // yiAn.details.toArray()[0];
            assertEquals("check description", description2, detail2.content);
            checkHerbs(herbs2, IterableUtils.get(detail2.prescriptions, 0).items);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void test6() {

        try {

            // 又 今年天符岁会，上半年阳气大泄，.....
            // 人参（一钱） 生牡蛎（五钱） 生白芍（二钱）
            // 上午服。
            // （丸方） 人参（二两） 茯苓（三两，生）
            // 为末，竹沥法丸，早上服三钱，百滚汤下。

            ArrayList<Token> tokens = new ArrayList<Token>();
            String description = "又 今年天符岁会，上半年阳气大泄，.....";
            tokens.add(new Token(TokenType.Description, description));
            String recipe1 = "人参（一钱） 生牡蛎（五钱） 生白芍（二钱）";
            tokens.add(new Token(TokenType.LiteralText, recipe1));
            tokens.add(new Token(TokenType.LiteralText, "上午服。"));
            ArrayList<Token> herbs2 = new ArrayList<Token>();
            herbs2.add(new Token(TokenType.Herb, "人参"));
            herbs2.add(new Token(TokenType.Herb, "茯苓"));
            tokens.addAll(herbs2);
            tokens.add(new Token(TokenType.LiteralText, "为末，竹沥法丸，早上服三钱，百滚汤下。"));
            tokens.add(new Token(TokenType.End));

            Grammar grammar = new Grammar(mGrammarFile);
            YiAnParser parser = new YiAnParser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test6.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.name = "test6";
            entity.book = new BookEntity();
            entity.book.name = "debug";
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager);
            node.accept(yiAnBuilder);

            List<YiAnEntity> yiAns = yiAnBuilder.getYiAns();
            assertEquals("only 2 YiAn", 1, yiAns.size());
            YiAnEntity yiAn1 = yiAns.get(0);
            assertEquals("only 1 YiAn Detail", 1, yiAn1.details.size());
            YiAnDetailEntity detail1 = IterableUtils.get(yiAn1.details, 0);// (YiAnDetailEntity)
                                                                           // yiAn.details.toArray()[0];
            assertEquals("check description", description, detail1.content);
            checkHerbs(recipe1, IterableUtils.get(detail1.prescriptions, 0).items);
            checkHerbs(herbs2, IterableUtils.get(detail1.prescriptions, 1).items);

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    private void checkHerbs(String expect, Collection<YiAnPrescriptionItemEntity> actual) {
        if (expect == null || expect.isEmpty()) {
            assertEquals("check empty herb compositions", 0, actual.size());
            return;
        }

        PrescriptionItemTokenExtractor tokenExtractor = new PrescriptionItemTokenExtractor();
        List<Token> tokens = new ArrayList<Token>();
        tokenExtractor.extract(expect, tokens);

        List<Token> herbs = new ArrayList<Token>();// tokens.stream().filter(x->TokenType.Herb.equals(x.getType())).collect(Collectors.toList());
        for (Token token : tokens) {
            if (TokenType.Herb.name().equals(token.getType())) {
                herbs.add(token);
            }
        }
        checkHerbs(herbs, actual);
    }

    private void checkHerbs(List<Token> expectHerbs, Collection<YiAnPrescriptionItemEntity> actual) {

        assertEquals("check tokens", expectHerbs.size(), actual.size());

        String errorMessage = "";

        for (Token token : expectHerbs) {
            if (!TokenType.Herb.name().equals(token.getType())) {
                continue;
            }
            String herb = token.getValue();
            Optional<YiAnPrescriptionItemEntity> entity = actual.stream().filter(x -> x.herb.equals(herb)).findFirst();
            if (entity == null || !entity.isPresent()) {
                errorMessage += " " + herb;
            }
        }

        if (!errorMessage.isEmpty()) {
            fail(errorMessage + " is missing");
        }
    }

    private String getActualFilePath(String fileName) {
        return "resource/YiAnParserTest/actual/" + fileName;
    }

    private String getExpectFilePath(String fileName) {
        return "resource/YiAnParserTest/expect/" + fileName;
    }

    private static String mGrammarFile;
    private static HerbAliasManager mHerbAliasManager;
    private static Utils mUtils;
    // private static String sTestResultFolder = "resource/YiAnParserTest/";
}
