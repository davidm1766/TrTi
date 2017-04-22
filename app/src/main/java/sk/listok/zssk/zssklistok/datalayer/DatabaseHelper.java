package sk.listok.zssk.zssklistok.datalayer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Nexi on 20.03.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "database";
    public final static String DATABASE_PATH = "/data/data/sk.listok.zssk.zssklistok/databases/";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        try {
            createDatabase(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     *
     * @param forceCreate - natvrdo prevali DB
     * @throws IOException
     */
    public void createDatabase(boolean forceCreate) throws IOException
    {
        boolean dbExist = checkDataBase();


        if (forceCreate){
            try
            {
                this.close();
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
        if(dbExist)
        {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDataBase();
        if(!dbExist1)
        {
            this.getReadableDatabase();
            try
            {
                this.close();
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }

    }
    //Check database already exist or not
    private boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(SQLiteException e)
        {
            e.printStackTrace();
        }
        return checkDB;
    }
    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException
    {

        InputStream mInput = myContext.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[2024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }
    //delete database
    public void db_delete()
    {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if(file.exists())
        {
            file.delete();
            System.out.println("delete database file.");
        }
    }
    //Open database
    public void openDatabase() throws SQLException
    {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
        {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }


    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        this.openDatabase();
        Cursor c = this.myDataBase.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
        this.close();
        return c;
    }

    public void executeSQL(String sql){

        try {
            this.openDatabase();
            myDataBase.beginTransaction();
            this.myDataBase.execSQL(sql);
            this.myDataBase.setTransactionSuccessful();
            this.close();
        }catch (Exception ex ) {
            ex.printStackTrace();
        } finally {
            this.myDataBase.endTransaction();
        }
    }



    public void test(){
       // ConnectionSource connectionSource = new AndroidConnectionSource(sqliteOpenHelper);

    }
}
