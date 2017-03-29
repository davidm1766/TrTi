package sk.listok.zssk.zssklistok.dataLayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.compat.BuildConfig;
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

    String DB_PATH = null;
    private static String DB_NAME = "database";
    private SQLiteDatabase myDatabse;
    private final Context myContext;


    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,10);
        this.myContext = context;
        this.DB_PATH = "/data/data/"+ context.getPackageName() +"/databases/";
        Log.e("paths 1",DB_PATH);
    }

    public void createDatabase() throws IOException{
        boolean dbExist =false;//checkDatabase();
        if(dbExist){

        }else{
            this.getReadableDatabase();
            try{
                copydatabase();
            }catch (IOException ex){
                throw new Error("Chyba pri koprovani DB");
            }
        }
    }

    private boolean checkDatabase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory()){

            }
            File dbFile = myContext.getDatabasePath(DB_NAME+"1.db");
             dbFile.exists();


            checkDB = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);
        }catch (Exception ex){
            throw new Error("nepodarilo sa otvorit DB");
        }
        if (checkDB !=null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }


    private void copydatabase() throws IOException{
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while((length = myInput.read(buffer)) > 0){
            myOutput.write(buffer,0,length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }



    public void openDatabase() throws IOException {
        String myPath = DB_PATH + DB_NAME;
        myDatabse = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close(){
        if(myDatabse != null){
            myDatabse.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            try {
                copydatabase();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public Cursor query(String table, String[] coluns, String selection, String[] selectionArgs, String groupBy, String Having, String orderBy){
        return myDatabse.query("TOWN",null,null,null,null,null,null);
    }


    public void test(){
       // ConnectionSource connectionSource = new AndroidConnectionSource(sqliteOpenHelper);

    }
}
