package com.hubert.parser.AST.YiAn;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.AST.IVisitor;
import com.hubert.parser.AST.YiAn.builder.*;

public class YiAnBuilderVisitor implements IVisitor {
	public YiAnBuilderVisitor(SectionEntity parentSection, HerbAliasManager herbAliasManager) {
		this(herbAliasManager);
		mParentSection = parentSection;

		SectionEntity currentSection = parentSection;
		while(mBook == null && currentSection != null){
			mBook = currentSection.book;
			currentSection = currentSection.parent;
		}
		mLogPath = "resource/" + mBook.name + "/debug_" + mParentSection.name + ".txt";
	}
	
	public YiAnBuilderVisitor(String logFolder, HerbAliasManager herbAliasManager){
		this(herbAliasManager);		
		mLogPath = logFolder;
	}
	
	private YiAnBuilderVisitor(HerbAliasManager herbAliasManager){
		new YiAnBuilder(this);
		mHerbAliasManager = herbAliasManager;
	}

	@Override
	public void visit(ASTNode node) {
		IYiAnBuilder builder = getBuilder(node);
		if (builder != null) {
			builder.build(node);
		}
		int childCount = node.childCount();
		for (int i = 0; i < childCount; ++i) {
			node.getChild(i).accept(this);
		}
		if (node.getParent() == null) {
			adjustYiAnDetails();
		}
	}

	private void adjustYiAnDetails() {
		try {
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
	
	public void registerBuilder(String type, IYiAnBuilder builder) {
		mBuilders.put(type, builder);
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
				for(String herb : herbs){
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
				// mFileWriter.write("Head:" );
				mFileWriter.write("summary:" + prescription.summary + "\n");
				mFileWriter.write("comment:" + prescription.comment + "\n");
			}
		}
	}

	private IYiAnBuilder getBuilder(ASTNode node) {
		String tag = node.getTag();
		String key = tag;
		if (YiAnNodeConstants.RecipeCompositionHerbOnly.equals(tag)) {
			key = YiAnNodeConstants.RecipeComposition;
		}
		if (mBuilders.containsKey(key)) {
			return mBuilders.get(key);
		}
		return null;
	}

	public void AddYiAn(YiAnEntity yiAn) {
		mYiAns.add(yiAn);
	}

	public List<YiAnEntity> getYiAns() {
		return mYiAns;
	}

	private Map<String, IYiAnBuilder> mBuilders = new HashMap<String, IYiAnBuilder>();
	private List<YiAnEntity> mYiAns = new ArrayList<YiAnEntity>();
	private SectionEntity mParentSection;
	private BookEntity mBook;

	private FileWriter mFileWriter;
	private String mLogPath;
	private HerbAliasManager mHerbAliasManager;

}
