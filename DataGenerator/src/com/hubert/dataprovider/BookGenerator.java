package com.hubert.dataprovider;

import java.io.*;
import java.nio.file.Paths;
//import java.nio.charset.Charset;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.sql.SQLException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;
//import com.hubert.parser.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.YiAn.*;
import com.hubert.parser.LL1.*;
import com.hubert.parser.tokenextractor.*;

public class BookGenerator {

    public BookGenerator(String grammarFilePath, String directory, HerbAliasManager herbAliasManager) throws Exception {
        //mBookDirectory = new File("resource/" + bookName);
        mBookDirectory = Paths.get(directory).toFile();
        mBook = new BookEntity();
        mBook.name = mBookDirectory.getName();//Paths.get(directory).getFileName();
        mBook.sections = new ArrayList<SectionEntity>();

        mYiAnParser = new YiAnParser();
        mGrammar = new Grammar(grammarFilePath);
        
        mHerbAliasManager = herbAliasManager;
    }

    public List<YiAnEntity> doImport() {
        try {
            loadSections(null, mBookDirectory);

            // TODO: requires better design here.
            // mYiAnParser.adjust();
            // mYiAnParser.validate();
            // mYiAnParser.save();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mYiAns;
    }

    private void loadSections(SectionEntity parent, File directory) throws Exception {

        File[] files = directory.listFiles();
        Arrays.sort(files);

        for (File file : files) {
            String fileName = file.getName();
            if (fileName.indexOf("summary") > 0 || fileName.indexOf("ignore") > 0 || fileName.indexOf("debug") >= 0) {
                System.out.println("ignore " + fileName);
                continue;
            }

            if (file.isDirectory()) {
                String sectionName = getSectionName(fileName);
                SectionEntity section = createSection(parent, sectionName);
                loadSections(section, file);
            }

            if (file.isFile()) {
                if (!fileName.endsWith(".txt")) {
                    System.out.println("ignore " + fileName);
                    continue;
                }
                loadBlocks(parent, file);
                break;
            }
        }
    }

    private void loadBlocks(SectionEntity parent, File file) throws Exception {
        String filePath = file.getAbsolutePath();
        YiAnLexer lexer = new YiAnLexer(filePath);
        List<Token> tokens = lexer.parse();

        String tokenFilePath = "resource/debug/" + file.getName() + "_token.text";
        Paths.get(tokenFilePath).getParent().toFile().mkdirs();
        FileWriter writer = new FileWriter(tokenFilePath);
        for (Token token : tokens) {
            writer.write(token.getType() + ":" + token.getValue() + "\n");
        }
        writer.close();
        ASTNode node = mYiAnParser.parse(mGrammar, tokens);
        LogVisitor visitor = new LogVisitor("resource/debug/" + file.getName() + "_AST.json");
        node.accept(visitor);

        YiAnBuilderVisitor builder = new YiAnBuilderVisitor(file.getAbsolutePath() + "_debug.txt",
                mHerbAliasManager);
        node.accept(builder);
        mYiAns.addAll(builder.getYiAns());
        return;
    }

    private SectionEntity createSection(SectionEntity parent, String sectionName) {
        SectionEntity section = new SectionEntity();
        // section.book = mBook;
        section.name = sectionName;
        section.blocks = new ArrayList<BlockEntity>();
        section.childSections = new ArrayList<SectionEntity>();

        section.order = mSectionOrderGenerator.nextOrder();

        if (parent != null) {
            section.parent = parent;
            parent.childSections.add(section);
        } else {
            section.parent = null;
            section.book = mBook;
            mBook.sections.add(section);
        }
        return section;
    }

    // 1.中风
    // 2.中风.txt
    private String getSectionName(String fileName) {
        int index = fileName.indexOf(".");
        fileName = fileName.substring(index + 1).trim();

        index = fileName.indexOf(".");
        if (index > 0) {
            return fileName.substring(0, index);
        }
        return StringUtils.strip(fileName);
    }

    protected File mBookDirectory;
    protected Grammar mGrammar;
    protected BookEntity mBook;
    protected OrderGenerator mSectionOrderGenerator = new OrderGenerator();

    protected YiAnParser mYiAnParser = new YiAnParser();
    
    protected HerbAliasManager mHerbAliasManager;

    protected List<YiAnEntity> mYiAns = new ArrayList<YiAnEntity>();
}
