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

            TableUtils.createTable(connectionSource, PrescriptionAliasEntity.class);
            TableUtils.createTable(connectionSource, BookEntity.class);
            TableUtils.createTable(connectionSource, SectionEntity.class);
            TableUtils.createTable(connectionSource, BlockEntity.class);
            TableUtils.createTable(connectionSource, PrescriptionEntity.class);
            TableUtils.createTable(connectionSource, PrescriptionUnitEntity.class);
            TableUtils.createTable(connectionSource, PrescriptionBlockLinkEntity.class);

            TableUtils.createTable(connectionSource, YiAnEntity.class);
            TableUtils.createTable(connectionSource, YiAnDetailEntity.class);
            TableUtils.createTable(connectionSource, YiAnPrescriptionEntity.class);
            TableUtils.createTable(connectionSource, YiAnPrescriptionItemEntity.class);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
