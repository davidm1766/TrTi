package sk.listok.zssk.zssklistok.datalayer.objects;

import android.database.Cursor;

import java.util.ArrayList;

import sk.listok.zssk.zssklistok.datalayer.DatabaseHelper;

/**
 * Konkretná implementácia {@link IQueryPerson}
 */
public class QueryPerson implements IQueryPerson {

    private DatabaseHelper dbhelper;

    public QueryPerson(DatabaseHelper dbhelper) {
        this.dbhelper = dbhelper;
    }

    @Override
    public ArrayList<Person> getAllPerson() {
        dbhelper.openDatabase();
        Cursor c = dbhelper.query("PERSON", null, null, null, null, null, null);
        ArrayList<Person> all = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                // ID, name, surname, email, regnumber, idcard
                all.add(new Person(c.getInt(0), c.getString(1),
                        c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
            } while (c.moveToNext());
        }
        c.close();
        dbhelper.close();
        return all;
    }

    @Override
    public Person getPersonByID(int ID) {
        dbhelper.openDatabase();
        Cursor c = dbhelper.query("PERSON", null, "ID=" + ID, null, null, null, null);
        Person p = null;
        int indexId = c.getColumnIndex("ID");
        int indexName = c.getColumnIndex("NAME");
        int indexSurn = c.getColumnIndex("SURNAME");
        int indexEmail = c.getColumnIndex("EMAIL");
        int indexReg = c.getColumnIndex("REG_NUMBER");
        int indexIdCard = c.getColumnIndex("ID_CARD");

        if (c.moveToFirst()) {
            do {
                // ID, name, surname, email, regnumber, idcard
                p = new Person(c.getInt(indexId), c.getString(indexName),
                        c.getString(indexSurn), c.getString(indexEmail), c.getString(indexReg), c.getString(indexIdCard));
            } while (c.moveToNext());
        }
        c.close();
        dbhelper.close();
        return p;
    }

    @Override
    public void addPerson(Person person) {
        String sql = "INSERT INTO PERSON (NAME, SURNAME, EMAIL, REG_NUMBER, ID_CARD) VALUES('" + person.getName() +
                "','" + person.getSurname() + "','" + person.getEmail() + "','" + person.getRegNumber() + "','" + person.getIdCard() + "')";
        this.dbhelper.executeSQL(sql);
    }

    @Override
    public void removePerson(Person person) {
        this.removePerson(person.getId());
    }

    @Override
    public void removePerson(int ID) {
        String sql = "DELETE FROM PERSON WHERE ID = " + ID;
        this.dbhelper.executeSQL(sql);
    }


    @Override
    public ArrayList<Person> getPersonByName(String name, String surname) {
        dbhelper.openDatabase();
        Cursor c = dbhelper.query("PERSON", null, "NAME='" + name + "' AND SURNAME='" + surname + "'", null, null, null, null);
        ArrayList<Person> p = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                // ID, name, surname, email, regnumber, idcard
                p.add(new Person(c.getInt(0), c.getString(1),
                        c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
            } while (c.moveToNext());
        }
        c.close();
        dbhelper.close();
        return p;
    }
}
