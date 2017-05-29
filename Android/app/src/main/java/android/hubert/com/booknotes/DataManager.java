package android.hubert.com.booknotes;


import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import com.hubert.dal.*;
import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.hubert.dal.entity.BookEntity;

public class DataManager {
    public BookEntity load(int id) {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(Constant.DATABASE_URL);
            Dao<BookEntity, Integer> bookDao = DaoManager.createDao(connectionSource, BookEntity.class);
            BookEntity entity = bookDao.queryForId(id);
            connectionSource.close();
            return entity;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
