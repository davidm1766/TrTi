package sk.listok.zssk.zssklistok.datalayer.objects;

import java.util.ArrayList;

/**
 *  Interface na základné operácie s tabuľkou osob(údaje o osobe)
 */
public interface IQueryPerson {
    ArrayList<Person> getAllPerson();
    Person getPersonByID(int ID);
    ArrayList<Person> getPersonByName(String name, String surname);
    void addPerson(Person person);
    void removePerson(Person person);
    void removePerson(int ID);
}
