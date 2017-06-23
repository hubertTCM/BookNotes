package com.hubert.dataprovider;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;

public class BookGenerator {

	public BookGenerator(String name) {
		mBookDirectory = new File("resource/" + name);
		mBook = new BookEntity();
		mBook.name = name;
		mBook.sections = new ArrayList<SectionEntity>();

		// TODO: requires better design here.
		mYiAnParser = new YiAnParser();
	}

	public void doImport() {
		try {
			loadSections(null, mBookDirectory);

			// TODO: requires better design here.
			mYiAnParser.validate();
			//mYiAnParser.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadSections(SectionEntity parent, File directory) throws IOException {

		File[] files = directory.listFiles();
		Arrays.sort(files);

		for (File file : files) {
			String fileName = file.getName();
			if (fileName.indexOf("summary") > 0) {
				continue;
			}

			if (file.isDirectory()) {
				String sectionName = getSectionName(fileName);
				SectionEntity section = createSection(parent, sectionName);
				loadSections(section, file);
			}

			if (file.isFile()) {
				if (!fileName.endsWith(".txt")){
					System.out.println("ignore " + fileName);
					continue;
				}
				loadBlocks(parent, file);
			}
		}
	}

	private void loadBlocks(SectionEntity parent, File file) throws IOException {
		Path filePath = Paths.get(file.getAbsolutePath());
		Charset utf8 = Charset.forName("UTF-8");
		
		//mBlockParser.SetSection(parent);

		List<String> lines = Files.readAllLines(filePath, utf8);
		
		// TODO: requires better design here.
		mYiAnParser.setParentSection(parent); 
		
		AbstractSingleLineParser temp = mYiAnParser;
		for (String line : lines) {
			line = StringUtils.strip(line);
			//mBlockParser.parse(line);
			temp = temp.parse(line);
		}
	}

	private SectionEntity createSection(SectionEntity parent, String sectionName) {
		SectionEntity section = new SectionEntity();
		section.book = mBook;
		section.name = sectionName;
		section.blocks = new ArrayList<BlockEntity>();
		section.childSections = new ArrayList<SectionEntity>();

		section.order = mSectionOrderGenerator.nextOrder();

		if (parent != null) {
			section.parent = parent;
			parent.childSections.add(section);
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
	protected BookEntity mBook;
	protected OrderGenerator mSectionOrderGenerator = new OrderGenerator();

	protected YiAnParser mYiAnParser = null;
}
