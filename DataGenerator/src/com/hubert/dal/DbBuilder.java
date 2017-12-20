package com.hubert.dal;

import java.io.File;
//import java.nio.file.*;
import java.sql.SQLException;

import com.hubert.dal.entity.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DbBuilder {

    public void build() {
        try {
            File file = new File("db/tcm.db");
            file.delete();

            JdbcConnectionSource connectionSource = new JdbcConnectionSource(Constant.DATABASE_URL);

            TableUtils.createTable(connectionSource, BookEntity.class);
            TableUtils.createTable(connectionSource, SectionEntity.class);
            TableUtils.createTable(connectionSource, BlockEntity.class);

            TableUtils.createTable(connectionSource, PrescriptionEntity.class);
            TableUtils.createTable(connectionSource, PrescriptionItem.class);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
