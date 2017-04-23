package sk.listok.zssk.zssklistok.datalayer.objects;

import android.database.Cursor;

import java.util.ArrayList;

import sk.listok.zssk.zssklistok.datalayer.DatabaseHelper;

/**
 * Konkretná implementácia {@link IQueryTown}
 */
public class QueryTown implements IQueryTown{

    private DatabaseHelper dbhelper;
    /**
     * Cachovanie železničných zastávok.
     */
    private ArrayList<Town> cachedTowns;

    public QueryTown(DatabaseHelper dbhelper){
        this.dbhelper = dbhelper;
    }




    @Override
    public ArrayList<Town> getAllTowns() {
        dbhelper.openDatabase();
        Cursor c = dbhelper.query("TOWN",null,null,null,null,null,null);
        ArrayList<Town> all = new ArrayList<>();
        int indexName =c.getColumnIndex("NAME");
        int indexId = c.getColumnIndex("ID");
        if(c.moveToFirst()) {
            do {        //1,0
                all.add(new Town(c.getString(indexName), c.getInt(indexId)));
            } while (c.moveToNext());
        }
        c.close();
        dbhelper.close();
        return all;
    }

    @Override
    public ArrayList<Town> getCachedTowns() {
        if(this.cachedTowns == null || this.cachedTowns.size() == 0){
            cachedTowns = getAllTowns();
        }
        return cachedTowns;
    }

    @Override
    public Town getTownByID(int ID) {
        dbhelper.openDatabase();
        Cursor c = dbhelper.query("TOWN",null,"ID="+ID,null,null,null,null);
        Town town = null;
        if(c.moveToFirst()) {
            do {
                town = new Town(c.getString(1), c.getInt(0));
            } while (c.moveToNext());
        }
        c.close();
        dbhelper.close();
        return town;
    }
}
