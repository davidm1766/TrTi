package sk.listok.zssk.zssklistok.dataLayer.objects;

import java.util.ArrayList;

/**
 * Created by Nexi on 11.04.2017.
 */

public interface IQueryPerson {
    ArrayList<Person> getAllPerson();
    Person getPersonByID(int ID);
    void addPerson(Person person);
    void removePerson(Person person);
    void removePerson(int ID);
}
