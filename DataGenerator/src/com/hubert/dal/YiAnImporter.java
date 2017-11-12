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
    
    public void save(BookEntity book, List<YiAnEntity> yiAns){
        try {
            mConnectionSource = new JdbcConnectionSource(Constant.DATABASE_URL);

            initDaoObjects();
            
            internalSave(book);

            mConnectionSource.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void internalSave(BookEntity book) throws SQLException{
        mBookDao.createOrUpdate(book);
        for(SectionEntity section : book.sections){
            internalSave(section);
        }
    }
    
    private void internalSave(SectionEntity section) throws SQLException{
        mSectionDao.createOrUpdate(section);
        for(BlockEntity block : section.blocks){
            mBlockDao.createOrUpdate(block);
        }
        for(SectionEntity child : section.childSections){
            internalSave(child);
        }
    }
    
    private void initDaoObjects() throws SQLException {
        mBookDao = DaoManager.createDao(mConnectionSource, BookEntity.class);
        mSectionDao = DaoManager.createDao(mConnectionSource, SectionEntity.class);
        mBlockDao = DaoManager.createDao(mConnectionSource, BlockEntity.class);

        mPrescriptionDao = DaoManager.createDao(mConnectionSource, PrescriptionEntity.class);
        mPrescriptionUnitDao = DaoManager.createDao(mConnectionSource, PrescriptionUnitEntity.class);
        mPrescriptionBlockLinkDao = DaoManager.createDao(mConnectionSource, PrescriptionBlockLinkEntity.class);
        mPrescriptionAliasDao = DaoManager.createDao(mConnectionSource, PrescriptionAliasEntity.class);
    }

    private ConnectionSource mConnectionSource;
    
    Dao<BookEntity, Integer> mBookDao;
    Dao<SectionEntity, Integer> mSectionDao;
    Dao<BlockEntity, Integer> mBlockDao;

    Dao<PrescriptionEntity, Integer> mPrescriptionDao;
    Dao<PrescriptionUnitEntity, Integer> mPrescriptionUnitDao;
    Dao<PrescriptionBlockLinkEntity, Integer> mPrescriptionBlockLinkDao;
    Dao<PrescriptionAliasEntity, Integer> mPrescriptionAliasDao;
}
