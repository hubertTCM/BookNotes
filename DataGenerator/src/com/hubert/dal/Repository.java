package com.hubert.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import com.hubert.dal.entity.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

public class Repository {
    public void create(Collection<YiAnEntity> yiAns) {
        try {
            _connectionSource = new JdbcConnectionSource(Constant.DATABASE_URL);

            for (YiAnEntity yiAnEntity : yiAns) {
                saveYiAn(yiAnEntity);
            }

            _connectionSource.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void create(YiAnEntity yiAnEntity) {

        try {
            _connectionSource = new JdbcConnectionSource(Constant.DATABASE_URL);

            saveYiAn(yiAnEntity);

            _connectionSource.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void saveYiAn(YiAnEntity yianEntity) throws SQLException {
        create(yianEntity, YiAnEntity.class);

        for (YiAnDetailEntity detailEntity : yianEntity.details) {
            detailEntity.yian = yianEntity;
            saveYiAnDetail(detailEntity);
        }
    }

    private void saveYiAnDetail(YiAnDetailEntity yiAnDetailEntity) throws SQLException {
        create(yiAnDetailEntity, YiAnDetailEntity.class);

        for (YiAnPrescriptionEntity item : yiAnDetailEntity.prescriptions) {
            item.yian = yiAnDetailEntity;
            if (item.items.isEmpty()) {
                System.out.println("ignore empty prescription");
                continue;
            }
            saveYiAnPrescription(item);
        }
    }

    private void saveYiAnPrescription(YiAnPrescriptionEntity entity) throws SQLException {
        create(entity, YiAnPrescriptionEntity.class);

        for (YiAnPrescriptionItemEntity item : entity.items) {
            item.yiAnPrescription = entity;
            create(item, YiAnPrescriptionItemEntity.class);
        }
    }

    private <T> void create(T entity, Class<T> type) throws SQLException {
        Dao<T, Integer> dao = DaoManager.createDao(_connectionSource, type);
        dao.create(entity);
    }

    private JdbcConnectionSource _connectionSource;
}
