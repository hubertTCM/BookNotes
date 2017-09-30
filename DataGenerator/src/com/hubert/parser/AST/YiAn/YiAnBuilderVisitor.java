package com.hubert.parser.AST.YiAn;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.IVisitor;
import com.hubert.parser.AST.YiAn.Evaluation.*;

public class YiAnBuilderVisitor implements IVisitor {
    public YiAnBuilderVisitor(SectionEntity parentSection, HerbAliasManager herbAliasManager) {
        this(herbAliasManager);
        mParentSection = parentSection;

        SectionEntity currentSection = parentSection;
        while (mBook == null && currentSection != null) {
            mBook = currentSection.book;
            currentSection = currentSection.parent;
        }
        mLogPath = "resource/" + mBook.name + "/debug_" + mParentSection.name + ".txt";
    }

    public YiAnBuilderVisitor(String logFolder, HerbAliasManager herbAliasManager) {
        this(herbAliasManager);
        mLogPath = logFolder;
    }

    private YiAnBuilderVisitor(HerbAliasManager herbAliasManager) {
        mHerbAliasManager = herbAliasManager;
        
        Context mContext = new Context(mYiAns);

        mEvaluators.add(new YiAnEvaluator(mContext));
        mEvaluators.add(new YiAnDetailEvaluator(mContext));
        mEvaluators.add(new YiAnDetailPropertyEvaluator(mContext));
        mEvaluators.add(new RecipeCompositionEvaluator(mContext));
        mEvaluators.add(new RecipeCompositionChildEvaluator(mContext));
        mEvaluators.add(new RecipeDetailEvaluator(mContext, mHerbAliasManager));
        mEvaluators.add(new RecipePropertyEvaluator(mContext));
    }

    @Override
    public void visit(ASTNode node) {
        System.out.println(node.getTag());
        IEvaluator evaluator = null;
        for (IEvaluator temp : mEvaluators) {
            if (temp.canEvaluate(node)) {
                evaluator = temp;
                evaluator.evaluate(node);
                break;
            }
        }

        int childCount = node.childCount();
        for (int i = 0; i < childCount; ++i) {
            node.getChild(i).accept(this);
        }

        System.out.println(node.getTag() + "*****");
        if (evaluator != null) {
            evaluator.postEvaluate(node);
        }

    }

    private void adjustYiAnDetails() {
        try {
            Paths.get(mLogPath).getParent().toFile().mkdirs();
            // File file = new File(mLogPath);
            // file.mkdirs();
            mFileWriter = new FileWriter(mLogPath);

            for (YiAnEntity item : mYiAns) {
                adjustYiAn(item);
                dump(item);
            }

            mFileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void adjustYiAn(YiAnEntity yiAn) {
        for (YiAnDetailEntity detail : yiAn.details) {
            for (YiAnPrescriptionEntity prescription : detail.prescriptions) {
                ArrayList<String> herbs = new ArrayList<String>();
                for (YiAnPrescriptionItemEntity item : prescription.items) {
                    String standardName = mHerbAliasManager.getStandardName(item.herb);
                    if (!herbs.contains(standardName)) {
                        herbs.add(standardName);
                    }
                }
                Collections.sort(herbs, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareToIgnoreCase(s2);
                    }
                });
                prescription.summary = "";
                for (String herb : herbs) {
                    prescription.summary += " " + herb;
                }
                prescription.summary = StringUtils.trim(prescription.summary);
            }
        }
    }

    private void dump(YiAnEntity yiAn) throws IOException {
        mFileWriter.write("YiAn\n");
        for (YiAnDetailEntity detail : yiAn.details) {
            mFileWriter.write("Content:" + detail.content + "\n");
            for (YiAnPrescriptionEntity prescription : detail.prescriptions) {
                mFileWriter.write("summary:" + prescription.summary + "\n");
                mFileWriter.write("comment:" + prescription.comment + "\n");
            }
        }
    }

    // private IYiAnBuilder getBuilder(ASTNode node) {
    // String tag = node.getTag();
    // String key = tag;
    // if (mBuilders.containsKey(key)) {
    // return mBuilders.get(key);
    // }
    // return null;
    // }

    public void AddYiAn(YiAnEntity yiAn) {
        mYiAns.add(yiAn);
    }

    public List<YiAnEntity> getYiAns() {
        return mYiAns;
    }

    private List<IEvaluator> mEvaluators = new ArrayList<IEvaluator>();
    private Context mContext;// = new Context(mYiAns);
    private List<YiAnEntity> mYiAns = new ArrayList<YiAnEntity>();
    private SectionEntity mParentSection;
    private BookEntity mBook;

    private FileWriter mFileWriter;
    private String mLogPath;
    private HerbAliasManager mHerbAliasManager;

}
