package sk.listok.zssk.zssklistok.datalayer.objects;

import java.util.ArrayList;

/**
 *  Interface na základné operácie s tabuľkou železničných
 *  staníc.
 */
public interface IQueryTown {
    ArrayList<Town> getAllTowns();
    ArrayList<Town> getCachedTowns();
    Town getTownByID(int ID);
}
