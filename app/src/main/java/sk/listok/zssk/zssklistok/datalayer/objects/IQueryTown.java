package sk.listok.zssk.zssklistok.datalayer.objects;

import java.util.ArrayList;

/**
 * Interface na základné operácie s tabuľkou železničných
 * staníc.
 */
public interface IQueryTown {

    /**
     * Vráti zoznam všekých staníc.
     */
    ArrayList<Town> getAllTowns();

    /**
     * Vráti všetky nakešované stanice.
     */
    ArrayList<Town> getCachedTowns();

    /**
     * Vráti stanicu podla ID.
     */
    Town getTownByID(int ID);
}
