package com.hubert;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.util.*;
//import java.util.stream.*; //it is too slow to use lambda...

import org.apache.commons.collections4.*;
import org.junit.*;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.dto.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.YiAn.*;
import com.hubert.parser.LL1.*;
import com.hubert.parser.tokenextractor.*;
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
            tokens.add(new YiAnToken(YiAnTokenType.Description, "阳挟内风上巅，目昏耳鸣不寐，肝经主病。", new Position(-1)));
            String recipe = "熟地（炙） 炙龟甲 萸肉 五味 磁石 茯苓 旱莲草 女贞子";
            tokens.add(new YiAnToken(YiAnTokenType.LiteralText, recipe, new Position(-1)));
            tokens.add(new YiAnToken(YiAnTokenType.End, new Position(-1)));

            Grammar grammar = new Grammar(mGrammarFile);
            Parser parser = new Parser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test1.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.book = new BookEntity();
            entity.name = "test1";
            entity.book.name = "debug";
            entity.blocks = new Vector<BlockEntity>();
            entity.childSections = new Vector<SectionEntity>();
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager, null);
            node.accept(yiAnBuilder);

            List<BlockGroupEntity> blockGroups = yiAnBuilder.getBlockGroups();
            List<BlockGroupEntity> yiAnBlockGroups = filterBlockGroups(blockGroups, BlockGroupTypeEnum.YiAn);
            assertEquals("only one YiAn", 1, yiAnBlockGroups.size());

            List<BlockGroupEntity> yiAnDetailGroups = filterBlockGroups(yiAnBlockGroups.get(0).children,
                    BlockGroupTypeEnum.YiAnDetail);
            assertEquals("only 1 YiAn Detail", 1, yiAnDetailGroups.size());

            List<Prescription> prescriptions = yiAnBuilder.getPrescriptions();
            assertEquals("only 1 prescriptions", 1, prescriptions.size());

            checkHerbs(recipe, IterableUtils.get(prescriptions, 0).getItems());
        } catch (Exception e) {
            e.printStackTrace(new PrintStream(System.out));
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
            tokens.add(new YiAnToken(YiAnTokenType.Description, description, new Position(-1)));
            tokens.add(new YiAnToken(YiAnTokenType.RecipeAbbreviation, "虎潜去锁阳、知母，加大肉苁蓉，炼蜜丸。", new Position(-1)));
            ArrayList<Token> herbs = new ArrayList<Token>();
            herbs.add(new YiAnToken(YiAnTokenType.Herb, "黄柏", new Position(-1)));
            herbs.add(new YiAnToken(YiAnTokenType.Herb, "龟板", new Position(-1)));
            herbs.add(new YiAnToken(YiAnTokenType.Herb, "生地黄", new Position(-1)));
            herbs.add(new YiAnToken(YiAnTokenType.Herb, "陈皮", new Position(-1)));
            tokens.addAll(herbs);
            tokens.add(new YiAnToken(YiAnTokenType.End, new Position(-1)));

            Grammar grammar = new Grammar(mGrammarFile);
            Parser parser = new Parser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test2.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.book = new BookEntity();
            entity.name = "test2";
            entity.book.name = "debug";
            entity.blocks = new Vector<BlockEntity>();
            entity.childSections = new Vector<SectionEntity>();
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager, null);
            node.accept(yiAnBuilder);

            List<BlockGroupEntity> blockGroups = yiAnBuilder.getBlockGroups();
            List<BlockGroupEntity> yiAnBlockGroups = filterBlockGroups(blockGroups, BlockGroupTypeEnum.YiAn);
            assertEquals("only one YiAn", 1, yiAnBlockGroups.size());

            List<BlockGroupEntity> yiAnDetailGroups = filterBlockGroups(yiAnBlockGroups.get(0).children,
                    BlockGroupTypeEnum.YiAnDetail);
            assertEquals("only 1 YiAn Detail", 1, yiAnDetailGroups.size());

            List<Prescription> prescriptions = yiAnBuilder.getPrescriptions();
            assertEquals("only 1 prescriptions", 1, prescriptions.size());

            checkHerbs(herbs, IterableUtils.get(prescriptions, 0).getItems());

            // TODO: verify it when DataProvider is ready for UT
            // BlockGroupEntity detailGroup = yiAnDetailGroups.get(0);
            // assertEquals("check description", description,
            // IterableUtils.get(detailGroup.blocks, 0).content);

        } catch (Exception e) {
            e.printStackTrace(new PrintStream(System.out));
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
            tokens.add(new YiAnToken(YiAnTokenType.Description, description1, new Position(-1)));
            tokens.add(new YiAnToken(YiAnTokenType.RecipeAbbreviation, "虎潜去锁阳、知母，加大肉苁蓉，炼蜜丸。", new Position(-1)));

            ArrayList<Token> herbs1 = new ArrayList<Token>();
            herbs1.add(new YiAnToken(YiAnTokenType.Herb, "黄柏", new Position(-1)));
            herbs1.add(new YiAnToken(YiAnTokenType.Herb, "龟板", new Position(-1)));
            herbs1.add(new YiAnToken(YiAnTokenType.Herb, "生地黄", new Position(-1)));
            herbs1.add(new YiAnToken(YiAnTokenType.Herb, "陈皮", new Position(-1)));
            tokens.addAll(herbs1);

            tokens.add(new YiAnToken(YiAnTokenType.Description, "又 前议苦辛酸降一法，肝风胃阳已折其上引之威，.....", new Position(-1)));
            tokens.add(new YiAnToken(YiAnTokenType.RecipeAbbreviation,
                    "九制熟地 肉苁蓉（用大而黑色者，去甲切片，盛竹篮内，放长流水中浸七日，晒干，以极淡为度，四两）.....", new Position(-1)));

            ArrayList<Token> herbs2 = new ArrayList<Token>();
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "黄柏", new Position(-1)));
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "龟板", new Position(-1)));
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "生地黄", new Position(-1)));
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "陈皮", new Position(-1)));
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "肉苁蓉", new Position(-1)));
            tokens.addAll(herbs2);

            tokens.add(new YiAnToken(YiAnTokenType.LiteralText, "上药照方制末，另用小黑稆豆皮八两煎浓汁，法丸，每早百滚水服三钱。", new Position(-1)));
            tokens.add(new YiAnToken(YiAnTokenType.RecipeHeader, "议晚上用健中运痰，兼制亢阳。火动风生，从《外台》茯苓饮意。", new Position(-1)));
            String recipe2 = "人参（二两） 熟半夏（二两） 茯苓（四两，生） 广皮肉（二两） 川连（姜汁炒，一两） 枳实（麸炒，二两） 明天麻（二两，煨）";
            tokens.add(new YiAnToken(YiAnTokenType.LiteralText, recipe2, new Position(-1)));
            tokens.add(new YiAnToken(YiAnTokenType.LiteralText, "上末，用竹沥一杯，姜汁十匙，法丸，食远开水服三钱。", new Position(-1)));

            tokens.add(new YiAnToken(YiAnTokenType.End, new Position(-1)));

            Grammar grammar = new Grammar(mGrammarFile);
            Parser parser = new Parser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test3.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.book = new BookEntity();
            entity.name = "test3";
            entity.book.name = "debug";
            entity.blocks = new Vector<BlockEntity>();
            entity.childSections = new Vector<SectionEntity>();
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager, null);
            node.accept(yiAnBuilder);

            List<BlockGroupEntity> blockGroups = yiAnBuilder.getBlockGroups();
            List<BlockGroupEntity> yiAnBlockGroups = filterBlockGroups(blockGroups, BlockGroupTypeEnum.YiAn);
            assertEquals("only one YiAn", 1, yiAnBlockGroups.size());

            List<BlockGroupEntity> yiAnDetailGroups = filterBlockGroups(yiAnBlockGroups.get(0).children,
                    BlockGroupTypeEnum.YiAnDetail);
            assertEquals("only 2 YiAn Detail", 2, yiAnDetailGroups.size());

            List<Prescription> prescriptions = yiAnBuilder.getPrescriptions();
            assertEquals("only 3 prescriptions", 3, prescriptions.size());

            Prescription p1 = IterableUtils.get(prescriptions, 0);
            checkHerbs(herbs1, p1.getItems());
            //
            checkHerbs(herbs2, IterableUtils.get(prescriptions, 1).getItems());
            checkHerbs(recipe2, IterableUtils.get(prescriptions, 2).getItems());
        } catch (Exception e) {
            e.printStackTrace(new PrintStream(System.out));
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
            tokens.add(new YiAnToken(YiAnTokenType.Description, description1, new Position(-1)));
            ArrayList<Token> herbs1 = new ArrayList<Token>();
            herbs1.add(new YiAnToken(YiAnTokenType.Herb, "黄柏", new Position(-1)));
            herbs1.add(new YiAnToken(YiAnTokenType.Herb, "龟板", new Position(-1)));
            herbs1.add(new YiAnToken(YiAnTokenType.Herb, "生地黄", new Position(-1)));
            herbs1.add(new YiAnToken(YiAnTokenType.Herb, "陈皮", new Position(-1)));
            herbs1.add(new YiAnToken(YiAnTokenType.Herb, "茯苓", new Position(-1)));
            tokens.addAll(herbs1);

            String description2 = "又 风热烁筋骨为痛，痰火气阻，呼吸不利，.....";
            tokens.add(new YiAnToken(YiAnTokenType.Description, description2, new Position(-1)));
            ArrayList<Token> herbs2 = new ArrayList<Token>();
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "人参", new Position(-1)));
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "半夏", new Position(-1)));
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "枳实", new Position(-1)));
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "茯苓", new Position(-1)));
            tokens.addAll(herbs2);

            tokens.add(new YiAnToken(YiAnTokenType.Description, "又", new Position(-1)));
            ArrayList<Token> herbs3 = new ArrayList<Token>();
            herbs3.add(new YiAnToken(YiAnTokenType.Herb, "黄柏", new Position(-1)));
            herbs3.add(new YiAnToken(YiAnTokenType.Herb, "龟板", new Position(-1)));
            herbs3.add(new YiAnToken(YiAnTokenType.Herb, "生地黄", new Position(-1)));
            herbs3.add(new YiAnToken(YiAnTokenType.Herb, "陈皮", new Position(-1)));
            tokens.addAll(herbs3);

            tokens.add(new YiAnToken(YiAnTokenType.End, new Position(-1)));

            Grammar grammar = new Grammar(mGrammarFile);
            Parser parser = new Parser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test4.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.book = new BookEntity();
            entity.name = "test4";
            entity.book.name = "debug";
            entity.blocks = new Vector<BlockEntity>();
            entity.childSections = new Vector<SectionEntity>();
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager, null);
            node.accept(yiAnBuilder);

            List<BlockGroupEntity> blockGroups = yiAnBuilder.getBlockGroups();
            List<BlockGroupEntity> yiAnBlockGroups = filterBlockGroups(blockGroups, BlockGroupTypeEnum.YiAn);
            assertEquals("only one YiAn", 1, yiAnBlockGroups.size());

            List<BlockGroupEntity> yiAnDetailGroups = filterBlockGroups(yiAnBlockGroups.get(0).children,
                    BlockGroupTypeEnum.YiAnDetail);
            assertEquals("only three YiAn Detail", 3, yiAnDetailGroups.size());

            List<Prescription> prescriptions = yiAnBuilder.getPrescriptions();
            assertEquals("only three prescriptions", 3, prescriptions.size());

            checkHerbs(herbs1, IterableUtils.get(prescriptions, 0).getItems());
            checkHerbs(herbs2, IterableUtils.get(prescriptions, 1).getItems());
            checkHerbs(herbs3, IterableUtils.get(prescriptions, 2).getItems());

        } catch (Exception e) {
            e.printStackTrace(new PrintStream(System.out));
            fail(e.toString());
        }
    }

    private List<BlockGroupEntity> filterBlockGroups(Collection<BlockGroupEntity> blockGroups,
            BlockGroupTypeEnum type) {
        List<BlockGroupEntity> yiAnBlockGroups = new Vector<BlockGroupEntity>();
        for (BlockGroupEntity temp : blockGroups) {
            if (temp.type.equals(type)) {
                yiAnBlockGroups.add(temp);
            }
        }
        return yiAnBlockGroups;
    }

    @Test
    public void test5() {
        try {

            ArrayList<Token> tokens = new ArrayList<Token>();
            String description1 = "阳挟内风上巅，目昏耳鸣不寐，肝经主病。";
            tokens.add(new YiAnToken(YiAnTokenType.Description, description1, new Position(-1)));
            ArrayList<Token> herbs1 = new ArrayList<Token>();
            herbs1.add(new YiAnToken(YiAnTokenType.LiteralText, "熟地（炙）", new Position(-1)));
            tokens.addAll(herbs1);
            tokens.add(new YiAnToken(YiAnTokenType.End, new Position(-1)));

            String description2 = "曾（五二）脉弦动，眩晕耳聋，行走气促无力，肛痔下垂。此未老欲衰，肾阴弱，收纳无权，肝阳炽，虚风蒙窍.......";
            tokens.add(new YiAnToken(YiAnTokenType.Description, description2, new Position(-1)));
            ArrayList<Token> herbs2 = new ArrayList<Token>();
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "黄柏", new Position(-1)));
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "龟板", new Position(-1)));
            tokens.addAll(herbs2);
            tokens.add(new YiAnToken(YiAnTokenType.End, new Position(-1)));

            Grammar grammar = new Grammar(mGrammarFile);
            Parser parser = new Parser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test5.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.book = new BookEntity();
            entity.name = "test5";
            entity.book.name = "debug";
            entity.blocks = new Vector<BlockEntity>();
            entity.childSections = new Vector<SectionEntity>();
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager, null);
            node.accept(yiAnBuilder);
            

            List<BlockGroupEntity> blockGroups = yiAnBuilder.getBlockGroups();
            List<BlockGroupEntity> yiAnBlockGroups = filterBlockGroups(blockGroups, BlockGroupTypeEnum.YiAn);
            assertEquals("only 2 YiAn", 2, yiAnBlockGroups.size());

            BlockGroupEntity yiAnGroup1 = yiAnBlockGroups.get(0);
            List<BlockGroupEntity> yiAnDetailGroups = filterBlockGroups(yiAnGroup1.children,
                    BlockGroupTypeEnum.YiAnDetail);
            assertEquals("only 1 YiAn Detail", 1, yiAnDetailGroups.size());
            

            BlockGroupEntity yiAnGroup2 = yiAnBlockGroups.get(0);
            List<BlockGroupEntity> yiAnDetail2Groups = filterBlockGroups(yiAnGroup2.children,
                    BlockGroupTypeEnum.YiAnDetail);
            assertEquals("only 1 YiAn Detail", 1, yiAnDetail2Groups.size());

            List<Prescription> prescriptions = yiAnBuilder.getPrescriptions();
            assertEquals("only 2 prescriptions", 2, prescriptions.size());

            Prescription p1 = IterableUtils.get(prescriptions, 0);
            checkHerbs(herbs1, p1.getItems());
            checkHerbs(herbs2, IterableUtils.get(prescriptions, 1).getItems());

        } catch (Exception e) {
            e.printStackTrace(new PrintStream(System.out));
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
            tokens.add(new YiAnToken(YiAnTokenType.Description, description, new Position(-1)));
            String recipe1 = "人参（一钱） 生牡蛎（五钱） 生白芍（二钱）";
            tokens.add(new YiAnToken(YiAnTokenType.LiteralText, recipe1, new Position(-1)));
            tokens.add(new YiAnToken(YiAnTokenType.LiteralText, "上午服。", new Position(-1)));
            ArrayList<Token> herbs2 = new ArrayList<Token>();
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "人参", new Position(-1)));
            herbs2.add(new YiAnToken(YiAnTokenType.Herb, "茯苓", new Position(-1)));
            tokens.addAll(herbs2);
            tokens.add(new YiAnToken(YiAnTokenType.LiteralText, "为末，竹沥法丸，早上服三钱，百滚汤下。", new Position(-1)));
            tokens.add(new YiAnToken(YiAnTokenType.End, new Position(-1)));

            Grammar grammar = new Grammar(mGrammarFile);
            Parser parser = new Parser();
            ASTNode node = parser.parse(grammar, tokens);

            String fileName = "ASTNode_test6.json";
            LogVisitor visitor = new LogVisitor(getActualFilePath(fileName));
            node.accept(visitor);
            mUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));

            SectionEntity entity = new SectionEntity();
            entity.name = "test6";
            entity.book = new BookEntity();
            entity.book.name = "debug";
            entity.blocks = new Vector<BlockEntity>();
            entity.childSections = new Vector<SectionEntity>();
            YiAnBuilderVisitor yiAnBuilder = new YiAnBuilderVisitor(entity, mHerbAliasManager, null);
            node.accept(yiAnBuilder);
            

            List<BlockGroupEntity> blockGroups = yiAnBuilder.getBlockGroups();
            List<BlockGroupEntity> yiAnBlockGroups = filterBlockGroups(blockGroups, BlockGroupTypeEnum.YiAn);
            assertEquals("only 1 YiAn", 1, yiAnBlockGroups.size());

            BlockGroupEntity yiAnGroup1 = yiAnBlockGroups.get(0);
            List<BlockGroupEntity> yiAnDetailGroups = filterBlockGroups(yiAnGroup1.children,
                    BlockGroupTypeEnum.YiAnDetail);
            assertEquals("only 1 YiAn Detail", 1, yiAnDetailGroups.size());
            

            BlockGroupEntity yiAnGroup2 = yiAnBlockGroups.get(0);
            List<BlockGroupEntity> yiAnDetail2Groups = filterBlockGroups(yiAnGroup2.children,
                    BlockGroupTypeEnum.YiAnDetail);
            assertEquals("only 1 YiAn Detail", 1, yiAnDetail2Groups.size());

            List<Prescription> prescriptions = yiAnBuilder.getPrescriptions();
            assertEquals("only 2 prescriptions", 2, prescriptions.size());

            Prescription p1 = IterableUtils.get(prescriptions, 0);
            checkHerbs(recipe1, p1.getItems());
            checkHerbs(herbs2, IterableUtils.get(prescriptions, 1).getItems());


        } catch (Exception e) {
            e.printStackTrace(new PrintStream(System.out));
            fail(e.toString());
        }
    }

    private void checkHerbs(String expect, Collection<PrescriptionItem> actual) {
        if (expect == null || expect.isEmpty()) {
            assertEquals("check empty herb compositions", 0, actual.size());
            return;
        }

        PrescriptionItemTokenExtractor tokenExtractor = new PrescriptionItemTokenExtractor();
        List<Token> tokens = new ArrayList<Token>();
        tokenExtractor.extract(expect, new Position(-1), tokens);

        List<Token> herbs = new ArrayList<Token>();// tokens.stream().filter(x->TokenType.Herb.equals(x.getType())).collect(Collectors.toList());
        for (Token token : tokens) {
            if (YiAnTokenType.Herb.name().equals(token.getType())) {
                herbs.add(token);
            }
        }
        checkHerbs(herbs, actual);
    }

    private void checkHerbs(List<Token> expectHerbs, Collection<PrescriptionItem> actual) {

        assertEquals("check tokens", expectHerbs.size(), actual.size());

        String errorMessage = "";

        for (Token token : expectHerbs) {
            if (!YiAnTokenType.Herb.name().equals(token.getType())) {
                continue;
            }
            String herb = token.getValue();
            Optional<PrescriptionItem> entity = actual.stream().filter(x -> x.herb.equals(herb)).findFirst();
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
