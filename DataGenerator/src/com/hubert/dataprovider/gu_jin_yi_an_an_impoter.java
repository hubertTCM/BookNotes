package com.hubert.dataprovider;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

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

			initDaoObjects();

			_book = new BookEntity();
			_book.name = getName();

			_bookDao.assignEmptyForeignCollection(_book, "sections");
			_bookDao.create(_book);

			loadSections(null, _bookDirectory);

			_connectionSource.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initDaoObjects() throws SQLException {
		_bookDao = DaoManager.createDao(_connectionSource, BookEntity.class);
		_sectionDao = DaoManager.createDao(_connectionSource, SectionEntity.class);
		_blockDao = DaoManager.createDao(_connectionSource, BlockEntity.class);

		_prescriptionDao = DaoManager.createDao(_connectionSource, PrescriptionEntity.class);
		_prescriptionUnitDao = DaoManager.createDao(_connectionSource, PrescriptionUnitEntity.class);
		_prescriptionBlockLinkDao = DaoManager.createDao(_connectionSource, PrescriptionBlockLinkEntity.class);
		_prescriptionAliasDao = DaoManager.createDao(_connectionSource, PrescriptionAliasEntity.class);
	}

	private void loadSections(SectionEntity parent, File directory) throws SQLException, IOException {
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
				loadBlocks(parent, file);
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

		_sectionDao.assignEmptyForeignCollection(section, "childSections");
		_sectionDao.assignEmptyForeignCollection(section, "blocks");

		if (parent != null) {
			section.parent = parent;
			// parent.childSections.add(section);
		}
		// _book.sections.add(section);
		// section may updated when _book.sections is created
		_sectionDao.createOrUpdate(section);
		return section;
	}

	private void loadBlocks(SectionEntity parentSection, File file) throws IOException, SQLException {
		Path filePath = Paths.get(file.getAbsolutePath());
		Charset utf8 = Charset.forName("UTF-8");

		String sectionName = getSectionName(file.getName());
		SectionEntity section = createSection(parentSection, sectionName);

		List<String> lines = Files.readAllLines(filePath, utf8);
		for (String line : lines) {
			if (line.indexOf("comment") > 0) {
				continue;
			}

			line = line.trim();
			if (line.isEmpty()) {
				continue;
			}

			parseContent(section, line);
		}
	}

	private void parseContent(SectionEntity section, String content) throws SQLException {
		if (content.indexOf("[") == 0) {
			parsePrescription(content);
			return;
		}

		_currentBlock = new BlockEntity();
		_currentBlock.content = content;
		_currentBlock.order = section.blocks.size() + 1;
		_currentBlock.section = section;
		// section.blocks.add(_currentBlock);
		_blockDao.createOrUpdate(_currentBlock);
	}

	private void parsePrescription(String content) throws SQLException {
		String prescriptionName = content.substring(1);
		int nameEndIndex = prescriptionName.indexOf(']');

		Map.Entry<String, List<String>> alias = parsePrescriptionAlias(prescriptionName.substring(0, nameEndIndex));
		for (String item : alias.getValue()) {
			PrescriptionAliasEntity aliasEntity = new PrescriptionAliasEntity();
			aliasEntity.alias = item;
			aliasEntity.name = alias.getKey();
			_prescriptionAliasDao.createOrUpdate(aliasEntity);
		}

		PrescriptionEntity prescription = new PrescriptionEntity();
		prescription.name = alias.getKey();
		_prescriptionDao.createOrUpdate(prescription);

		String prescriptionDetail = prescriptionName.substring(nameEndIndex + 1);

		PrescriptionBlockLinkEntity prescriptionBlockLink = new PrescriptionBlockLinkEntity();
		prescriptionBlockLink.block = _currentBlock;
		prescriptionBlockLink.prescription = prescription;
		_prescriptionBlockLinkDao.createOrUpdate(prescriptionBlockLink);

	}

	private Map.Entry<String, List<String>> parsePrescriptionAlias(String content) {
		int aliasStartIndex = content.indexOf('(');
		if (aliasStartIndex < 0) {
			return new AbstractMap.SimpleEntry<String, List<String>>(content, new ArrayList<String>());
		}

		String name = content.substring(0, aliasStartIndex);
		String[] temp = content.substring(aliasStartIndex + 1, content.length() - 1).split(" ");
		return new AbstractMap.SimpleEntry<String, List<String>>(name, Arrays.asList(temp));
	}

	// 1.卷一
	// 2.中风.txt
	private String getSectionName(String fileName) {
		int index = fileName.indexOf(".");
		fileName = fileName.substring(index + 1).trim();

		index = fileName.indexOf(".");
		if (index > 0) {
			return fileName.substring(0, index);
		}
		return fileName;
	}

	Dao<BookEntity, Integer> _bookDao;
	Dao<SectionEntity, Integer> _sectionDao;
	Dao<BlockEntity, Integer> _blockDao;

	Dao<PrescriptionEntity, Integer> _prescriptionDao;
	Dao<PrescriptionUnitEntity, Integer> _prescriptionUnitDao;
	Dao<PrescriptionBlockLinkEntity, Integer> _prescriptionBlockLinkDao;
	Dao<PrescriptionAliasEntity, Integer> _prescriptionAliasDao;

	private BookEntity _book;
	private BlockEntity _currentBlock;
	private ConnectionSource _connectionSource;
	private File _bookDirectory;
}
