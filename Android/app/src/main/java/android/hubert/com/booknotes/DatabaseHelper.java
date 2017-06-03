package android.hubert.com.booknotes;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hubert.dal.entity.BookEntity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // The Android's default system path of your application database.
    private static String DB_PATH = null;// "/data/data/YOUR_PACKAGE/databases/";

    private static String mDbName = "tcm.db";

    // private SQLiteDatabase myDataBase;

    private final Context mContext;

    /**
     * Constructor Takes and keeps a reference of the passed context in order to
     * access to the application assets and resources.
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, mDbName, null, 1);
        this.mContext = context;
        String packageName = context.getPackageName();
        DB_PATH = String.format("//data//data//%s//databases//", packageName);

        //onCreate is not invoked before write
        //https://stackoverflow.com/questions/10260137/ormlite-database-helper-oncreate-not-called
        createDatabase();
    }

    private void createDatabase() {
        try {
            File db = new File(DB_PATH, mDbName);
            if (!db.exists()) {
                new File(DB_PATH).mkdirs();
                db.createNewFile();
                copyFromZipFile();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void copyFromZipFile() throws IOException {
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.tcm);
        OutputStream outputStream = new FileOutputStream(new File(DB_PATH, mDbName).getAbsolutePath());
        try {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, count);
            }
        } finally {
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }
    }


    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            createDatabase();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            //TableUtils.dropTable(connectionSource, SimpleData.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public BookEntity load(int id) {
        try {
            //ConnectionSource connectionSource = new JdbcConnectionSource(Constant.DATABASE_URL);
            Dao<BookEntity, Integer> bookDao = getDao(BookEntity.class); //DaoManager.createDao(connectionSource, BookEntity.class);
            BookEntity entity = bookDao.queryForId(id);
            connectionSource.close();
            return entity;
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //}catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return null;
    }
}
