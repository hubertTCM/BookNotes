package com.hubert.dataprovider;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import com.hubert.dal.Constant;
import com.hubert.dal.entity.*;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class gu_jin_yi_an_an_impoter {

	public gu_jin_yi_an_an_impoter() {
		_bookDirectory = new File("resource/古今医案按");
	}

	public String getName() {
		return "古今医案按";
	}

	public void doImport() {
		try {
			_connectionSource = new JdbcConnectionSource(Constant.DATABASE_URL);
			_book = new BookEntity();
			_book.name = getName();

			Dao<BookEntity, Integer> dao = DaoManager.createDao(_connectionSource, BookEntity.class);
			dao.assignEmptyForeignCollection(_book, "sections");
			dao.create(_book);

			loadSections(null, _bookDirectory);

			save();

			_connectionSource.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadSections(SectionEntity parent, File directory) throws SQLException {
		// long order = 1;
		File[] listOfFiles = directory.listFiles();
		Arrays.sort(listOfFiles);

		
		for (File file : listOfFiles) {
			if (file.isDirectory()) {
				String sectionName = getSectionName(file.getName());
				SectionEntity section = createSection(parent, sectionName);

				loadSections(section, file);

				// order += 1;
			}
		}
	}

	private SectionEntity createSection(SectionEntity parent, String sectionName) throws SQLException {
		SectionEntity section = new SectionEntity();
		section.book = _book;
		section.name = sectionName;
		if (parent == null) {
			section.order = _book.sections.size() + 1;
		} else {
			section.order = parent.childSections.size() + 1;
		}

		Dao<SectionEntity, Integer> dao = DaoManager.createDao(_connectionSource, SectionEntity.class);
		dao.assignEmptyForeignCollection(section, "childSections");

		if (parent != null) {
			section.parent = parent;
			parent.childSections.add(section);
		}
		dao.create(section);
		_book.sections.add(section);
		return section;
	}

	private String getSectionName(String folderName) {
		int index = folderName.indexOf(".");
		return folderName.substring(index + 1).trim();
	}

	// http://stackoverflow.com/questions/12885499/problems-saving-collection-using-ormlite-on-android
	private void save() throws SQLException, IOException {

	}

	private BookEntity _book;
	private ConnectionSource _connectionSource;
	private File _bookDirectory;
}
