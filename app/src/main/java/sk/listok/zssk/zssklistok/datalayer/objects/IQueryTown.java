package sk.listok.zssk.zssklistok.datalayer.objects;

import java.util.ArrayList;

/**
 * Created by Nexi on 11.04.2017.
 */

public interface IQueryTown {
    ArrayList<Town> getAllTowns();
    ArrayList<Town> getCachedTowns();
    Town getTownByID(int ID);
}
