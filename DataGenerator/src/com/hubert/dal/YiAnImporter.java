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

    public void save(BookEntity book,
            List<BlockGroupEntity> blockGroups,
            List<BookReferenceEntity> prescriptions) {
        try {
            System.out.println("start update database");

            mConnectionSource = new JdbcConnectionSource(Constant.DATABASE_URL);

            initDaoObjects();

            internalSave(book);
            internalSave(blockGroups);
            
            for(BookReferenceEntity prescription : prescriptions){
                internalSave(prescription);
            }

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
            if (block.blockGroup != null){
                internalSave(block.blockGroup);
            }
            mBlockDao.createOrUpdate(block);
        }
        for (SectionEntity child : section.childSections) {
            internalSave(child);
        }
    }
    private void internalSave(Collection<BlockGroupEntity> blockGroups) throws SQLException{
        for(BlockGroupEntity item : blockGroups){
            internalSave(item);
        }
    }
    
    private void internalSave(BlockGroupEntity blockGroup) throws SQLException{

        if (blockGroup.parent != null){
            mBlockGroupDao.createOrUpdate(blockGroup.parent);
        }
        mBlockGroupDao.createOrUpdate(blockGroup);
        
        internalSave(blockGroup.children);
    }
    
    private void internalSave(BookReferenceEntity blockReference) throws SQLException{
        mBlockReferenceDao.createOrUpdate(blockReference);
    }

    private void initDaoObjects() throws SQLException {
        mBookDao = DaoManager.createDao(mConnectionSource, BookEntity.class);
        mSectionDao = DaoManager.createDao(mConnectionSource, SectionEntity.class);
        mBlockDao = DaoManager.createDao(mConnectionSource, BlockEntity.class);
        mBlockGroupDao = DaoManager.createDao(mConnectionSource, BlockGroupEntity.class);
        mBlockReferenceDao = DaoManager.createDao(mConnectionSource, BookReferenceEntity.class);
    }
    

    private ConnectionSource mConnectionSource;

    private Dao<BookEntity, Integer> mBookDao;
    private Dao<SectionEntity, Integer> mSectionDao;
    private Dao<BlockEntity, Integer> mBlockDao;
    private Dao<BlockGroupEntity, Integer> mBlockGroupDao;
    private Dao<BookReferenceEntity, Integer> mBlockReferenceDao;

}
