package com.hubert.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import com.hubert.dal.entity.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class YiAnImporter {

    public void save(BookEntity book, List<YiAnEntity> yiAns) {
        try {
            System.out.println("start update database");

            mConnectionSource = new JdbcConnectionSource(Constant.DATABASE_URL);

            initDaoObjects();

            internalSave(book);

            mConnectionSource.close();

            System.out.println("end update database");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void internalSave(BookEntity book) throws SQLException {
        mBookDao.createOrUpdate(book);
        for (SectionEntity section : book.sections) {
            internalSave(section);
        }
    }

    private void internalSave(SectionEntity section) throws SQLException {
        mSectionDao.createOrUpdate(section);
        for (BlockEntity block : section.blocks) {
            mBlockDao.createOrUpdate(block);
        }
        for (SectionEntity child : section.childSections) {
            internalSave(child);
        }
    }

    private void initDaoObjects() throws SQLException {
        mBookDao = DaoManager.createDao(mConnectionSource, BookEntity.class);
        mSectionDao = DaoManager.createDao(mConnectionSource, SectionEntity.class);
        mBlockDao = DaoManager.createDao(mConnectionSource, BlockEntity.class);
        }

    private ConnectionSource mConnectionSource;

    Dao<BookEntity, Integer> mBookDao;
    Dao<SectionEntity, Integer> mSectionDao;
    Dao<BlockEntity, Integer> mBlockDao;

}
