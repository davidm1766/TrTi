package sk.listok.zssk.zssklistok.dataLayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import sk.listok.zssk.zssklistok.R;


/**
 * Created by Nexi on 20.03.2017.
 */

public class OrmDatabaseHelper extends OrmLiteSqliteOpenHelper{


        private static final String DATABASE_NAME = "database";
        private static final int DATABASE_VERSION = 1;

        /**
         * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
         */
        private Dao<Town, Long> todoDao;

        public OrmDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);//,R.raw.ormlite_config);
        }

        @Override
        public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
            try {

                /**
                 * creates the Todo database table
                 */
                TableUtils.createTable(connectionSource, Town.class);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                              int oldVersion, int newVersion) {
            try {
                /**
                 * Recreates the database when onUpgrade is called by the framework
                 */
                TableUtils.dropTable(connectionSource, Town.class, false);
                onCreate(database, connectionSource);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Returns an instance of the data access object
         * @return
         * @throws SQLException
         */
        public Dao<Town, Long> getDao() throws SQLException {
            if(todoDao == null) {
                todoDao = getDao(Town.class);
            }
            return todoDao;
        }

}
