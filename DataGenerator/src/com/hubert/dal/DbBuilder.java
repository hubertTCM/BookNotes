package com.hubert.dal;

import java.sql.SQLException;

import com.hubert.dal.entity.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DbBuilder {

	public void build() {
		try {
			JdbcConnectionSource connectionSource = new JdbcConnectionSource(Constant.DATABASE_URL);
			TableUtils.createTable(connectionSource, BookEntity.class);
			TableUtils.createTable(connectionSource, SectionEntity.class);
			TableUtils.createTable(connectionSource, BlockEntity.class);
			TableUtils.createTable(connectionSource, NoteEntity.class);
			TableUtils.createTable(connectionSource, PrescriptionEntity.class);
			TableUtils.createTable(connectionSource, PrescriptionUnitEntity.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
