package sk.listok.zssk.zssklistok.dataLayer.objects;

import java.util.ArrayList;

/**
 * Created by Nexi on 11.04.2017.
 */

public interface IQueryTown {
    ArrayList<Town> getAllTowns();
    Town getTownByID(int ID);
}
