package sk.listok.zssk.zssklistok.datalayer.objects;

import java.util.ArrayList;

/**
 * Interface na základné operácie s tabuľkou osob(údaje o osobe)
 */
public interface IQueryPerson {
    /**
     * Vráti všetky osoby(osobné údaje)
     */
    ArrayList<Person> getAllPerson();

    /**
     * Vráti osobu podľa ID.
     */
    Person getPersonByID(int ID);

    /**
     * Vráti všetky osoby podľa mena a priezviska.
     */
    ArrayList<Person> getPersonByName(String name, String surname);

    /**
     * Pridá osobu.
     */
    void addPerson(Person person);

    /**
     * Odstráni osobu.
     */
    void removePerson(Person person);

    /**
     * Odstráni osobu podľa ID.
     */
    void removePerson(int ID);
}
