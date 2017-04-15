package sk.listok.zssk.zssklistok.dataLayer.objects;

import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;

import sk.listok.zssk.zssklistok.MainActivity;
import sk.listok.zssk.zssklistok.dataLayer.DatabaseHelper;

/**
 * Created by Nexi on 11.04.2017.
 */

public class QueryTown implements IQueryTown{

    private DatabaseHelper dbhelper;
    private ArrayList<Town> cachedTowns;

    public QueryTown(DatabaseHelper dbhelper){
        this.dbhelper = dbhelper;
    }




    @Override
    public ArrayList<Town> getAllTowns() {
        dbhelper.openDatabase();
        Cursor c = dbhelper.query("TOWN",null,null,null,null,null,null);
        ArrayList<Town> all = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                all.add(new Town(c.getString(1), c.getInt(0)));
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
