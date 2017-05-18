package com.hubert.dataprovider;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

			loadSections();
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

	private void loadSections() throws SQLException {
		File[] listOfFiles = _bookDirectory.listFiles();
		long order = 1;
		for (File file : listOfFiles) {
			if (file.isDirectory()) {
				String sectionName = getSectionName(file.getName());
				SectionEntity section = new SectionEntity();
				section.book = _book;
				section.name = sectionName;
				section.order = order;

				Dao<SectionEntity, Integer> dao = DaoManager.createDao(_connectionSource, SectionEntity.class);
				dao.assignEmptyForeignCollection(section, "childSections");

				_book.sections.add(section);
			}
		}
		// http://stackoverflow.com/questions/12885499/problems-saving-collection-using-ormlite-on-android
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
