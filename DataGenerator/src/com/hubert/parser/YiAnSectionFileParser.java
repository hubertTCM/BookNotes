package com.hubert.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.hubert.dal.entity.*;
import com.hubert.parser.tokenextractor.*;

public class YiAnSectionFileParser {
	public YiAnSectionFileParser(BookEntity book, String fileFullPath) {
		initSection(book.sections, fileFullPath);
		mSection.book = book;
	}

	public YiAnSectionFileParser(SectionEntity parentSection, String fileFullPath) {
		initSection(parentSection.childSections, fileFullPath);
		mSection.parent = parentSection;
	}
	
	public void parse(List<Token> tokens) throws IOException {
		mYiAns = new ArrayList<YiAnEntity>();
		if (tokens == null || tokens.isEmpty()){
			return;
		}
		
		YiAnEntity currentYiAn = createYiAn();
		
		YiAnDetailEntity currentDetail = null;
		YiAnPrescriptionEntity currentPrescription = null;
		//SectionEntity currentSection = mSection;
//		for(Token token : tokens){
//			TokenType tokenType = token.getType();
//			if (tokenType == TokenType.BlankSpace){
//				currentYiAn = createYiAn();
//				continue;
//			}
//			// TODO:
//			if (tokenType == TokenType.SectionName){
//				continue;
//			}
//			
//			if (tokenType == TokenType.YiAnDescription){
//				currentDetail  = createYiAnDetail(currentYiAn);
//				continue;
//			}
//			
//			if (tokenType== TokenType.PrescriptionFormatted){
//				currentPrescription = createYiAnPrescription(currentDetail);
//				continue;
//			}
//		}
	}

	/**
	 * @param currentYiAn
	 */
	protected YiAnDetailEntity createYiAnDetail(YiAnEntity currentYiAn) {
		YiAnDetailEntity currentDetail = new YiAnDetailEntity();
		currentDetail.yian = currentYiAn;
		currentDetail.order = currentYiAn.details.size();
		
		currentDetail.prescriptions = new ArrayList<YiAnPrescriptionEntity>();
		return currentDetail;
	}
	
	protected YiAnPrescriptionEntity createYiAnPrescription(YiAnDetailEntity yiAnDetail){
		YiAnPrescriptionEntity prescription = new YiAnPrescriptionEntity();
		yiAnDetail.prescriptions.add(prescription);
		prescription.yian = yiAnDetail;
		return prescription;
	}

	/**
	 * @return
	 */
	protected YiAnEntity createYiAn() {
		YiAnEntity currentYiAn = new YiAnEntity();
		currentYiAn.details = new ArrayList<YiAnDetailEntity>();
		mYiAns.add(currentYiAn);
		return currentYiAn;
	}

	public ArrayList<YiAnEntity> getYiAn() {
		return null;
	}

	public SectionEntity getSection() {
		return mSection;
	}

	private void initSection(Collection<SectionEntity> container, String sectionFileFullPath) {
		String sectionName = Utils.getSectionName(sectionFileFullPath);
		mSection = Utils.createSection(sectionName);
		mSection.order = container.size() + 1;
		container.add(mSection);
	}

	private SectionEntity mSection;
	private ArrayList<YiAnEntity> mYiAns;
}
