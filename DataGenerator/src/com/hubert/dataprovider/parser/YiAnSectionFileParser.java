package com.hubert.dataprovider.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.hubert.dal.entity.*;

public class YiAnSectionFileParser {
	public YiAnSectionFileParser(BookEntity book, String fileFullPath) {
		mSectionFileFullPath = fileFullPath;
		initSection(book.sections);
		mSection.book = book;
	}

	public YiAnSectionFileParser(SectionEntity parentSection, String fileFullPath) {
		mSectionFileFullPath = fileFullPath;
		initSection(parentSection.childSections);
		mSection.parent = parentSection;
	}

	public void parse() throws IOException {
		Path filePath = Paths.get(mSectionFileFullPath);
		Charset utf8 = Charset.forName("UTF-8");
		List<String> lines = Files.readAllLines(filePath, utf8);
		
		for (String line : lines) {
			
		}
	}

	public ArrayList<YiAnEntity> getYiAn() {
		return null;
	}

	public SectionEntity getSection() {
		return mSection;
	}

	private void initSection(Collection<SectionEntity> container) {
		String sectionName = Utils.getSectionName(mSectionFileFullPath);
		mSection = Utils.createSection(sectionName);
		mSection.order = container.size() + 1;
		container.add(mSection);
	}

	private String mSectionFileFullPath;
	private SectionEntity mSection;
}
