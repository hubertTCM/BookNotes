package com.hubert.dataprovider;

import java.io.*;
import java.nio.file.*;
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
import com.hubert.parser.tokenextractor.YiAn.*;

import javafx.util.Pair;

public class BookGenerator {

    public BookGenerator(String grammarFilePath, String directory, HerbAliasManager herbAliasManager) throws Exception {
        // mBookDirectory = new File("resource/" + bookName);
        mBookDirectory = Paths.get(directory).toFile();
        mBook = new BookEntity();
        mBook.name = mBookDirectory.getName();// Paths.get(directory).getFileName();
        mBook.sections = new ArrayList<SectionEntity>();

        mYiAnParser = new Parser();
        mGrammar = new Grammar(grammarFilePath);

        mHerbAliasManager = herbAliasManager;

        mDebugOutputDirectory = "resource/debug/" + mBook.name + "/";
        File debugDirectory = new File(mDebugOutputDirectory);
        debugDirectory.mkdirs();
    }

    public List<YiAnEntity> doImport() {
        try {
            loadSections(null, mBookDirectory);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mYiAns;
    }

    public List<SortedMap<Position, String>> getTokens() {
        return mTokens;
    }
    
    public BookEntity getBook(){
        return mBook;
    }

    private void loadSections(SectionEntity parent, File directory) throws Exception {

        File[] files = directory.listFiles();
        Arrays.sort(files);

        for (File file : files) {
            String fileName = file.getName();
            if (shouldIgnore(fileName)) {
                continue;
            }

            if (file.isDirectory()) {
                String sectionName = getSectionName(fileName);
                SectionEntity section = createSection(parent, sectionName);
                loadSections(section, file);
            }

            if (file.isFile()) {
                loadBlocks(parent, file);
                break;
            }
        }
    }

    private boolean shouldIgnore(String fileName) {
        if (fileName.endsWith(".pdf") || fileName.endsWith(".xml")) {
            return true;
        }

        if (fileName.indexOf("summary") > 0 || fileName.indexOf("ignore") > 0 || fileName.indexOf("debug") >= 0
                || fileName.indexOf("test") >= 0) {
            return true;
        }
        return false;
    }

    private void loadBlocks(SectionEntity parent, File file) throws Exception {
        String filePath = file.getAbsolutePath();
        YiAnLexer lexer = new YiAnLexer(filePath);
        List<Token> tokens = lexer.parse();

        ASTNode node = mYiAnParser.parse(mGrammar, tokens);

        YiAnBuilderVisitor builder = new YiAnBuilderVisitor(parent, mHerbAliasManager, lexer.getDataProvider());
        node.accept(builder);
        mYiAns.addAll(builder.getYiAns());
        mTokens.addAll(builder.getTokens());

        Pair<String, String> debugPathInfo = extractDebugDirectory(file);
        String astFilePath = Paths.get(debugPathInfo.getKey(), debugPathInfo.getValue() + "_AST.json").toString();
        LogVisitor visitor = new LogVisitor(astFilePath);
        node.accept(visitor);

        String tokenFilePath = Paths.get(debugPathInfo.getKey(), debugPathInfo.getValue() + "_token.txt").toString();
        logTokens(tokenFilePath, builder.getTokens(), lexer.getDataProvider());

        return;
    }

    protected void logTokens(String filePath, List<SortedMap<Position, String>> tokens, DataProvider provider) {
        try {
            FileWriter writer = new FileWriter(filePath);
            for (SortedMap<Position, String> temp : tokens) {
                for (Map.Entry<Position, String> entry : temp.entrySet()) {
                    String content = provider.getContent(entry.getKey());
                    content = Constants.TokenStartTag + entry.getValue() + Constants.TokenEndTag + content;
                    writer.write(content + "\n");
                }
                writer.write(Constants.TokenStartTag + Constants.End + Constants.TokenEndTag + "\n");
            }
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected Pair<String, String> extractDebugDirectory(File file) {
        // https://stackoverflow.com/questions/204784/how-to-construct-a-relative-path-in-java-from-two-absolute-paths-or-urls
        Path pathBase = Paths.get(mBookDirectory.getAbsolutePath());
        Path pathAbsolute = Paths.get(file.getParent()).toAbsolutePath();
        Path pathRelative = pathBase.relativize(pathAbsolute);
        String outputDirectory = Paths.get(mDebugOutputDirectory, pathRelative.toString()).toString();
        String fileNameWithoutExtension = file.getName();
        int pos = fileNameWithoutExtension.lastIndexOf(".");
        if (pos > 0) {
            fileNameWithoutExtension = fileNameWithoutExtension.substring(0, pos);
        }

        return new Pair<>(outputDirectory, fileNameWithoutExtension);
    }

    private SectionEntity createSection(SectionEntity parent, String sectionName) {
        SectionEntity section = new SectionEntity();
        section.name = sectionName;
        section.book = mBook;
        section.blocks = new ArrayList<BlockEntity>();
        section.childSections = new ArrayList<SectionEntity>();

        section.order = mSectionOrderGenerator.nextOrder();

        if (parent != null) {
            section.parent = parent;
            parent.childSections.add(section);
        } else {
            section.parent = null;
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
    protected String mDebugOutputDirectory;
    protected Grammar mGrammar;
    protected BookEntity mBook;
    protected OrderGenerator mSectionOrderGenerator = new OrderGenerator();

    protected Parser mYiAnParser = new Parser();

    protected HerbAliasManager mHerbAliasManager;

    protected List<YiAnEntity> mYiAns = new ArrayList<YiAnEntity>();
    private List<SortedMap<Position, String>> mTokens = new ArrayList<SortedMap<Position, String>>();
}
