package sk.listok.zssk.zssklistok.dataLayer.objects;

import android.database.Cursor;

import java.util.ArrayList;

import sk.listok.zssk.zssklistok.dataLayer.DatabaseHelper;

/**
 * Created by Nexi on 11.04.2017.
 */

public class QueryPerson implements IQueryPerson {



    private DatabaseHelper dbhelper;

    public QueryPerson(DatabaseHelper dbhelper){
        this.dbhelper = dbhelper;
    }

    @Override
    public ArrayList<Person> getAllPerson() {
        dbhelper.openDatabase();
        Cursor c = dbhelper.query("PERSON",null,null,null,null,null,null);
        ArrayList<Person> all = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                // ID, name, surname, email, regnumber, idcard
                all.add(new Person(c.getInt(0), c.getString(1),
                        c.getString(2),c.getString(3),c.getString(4),c.getString(5)));
            } while (c.moveToNext());
        }
        c.close();
        dbhelper.close();
        return all;
    }

    @Override
    public Person getPersonByID(int ID) {
        dbhelper.openDatabase();
        Cursor c = dbhelper.query("PERSON",null,"ID="+ID,null,null,null,null);
        Person p = null;
        if(c.moveToFirst()) {
            do {
                // ID, name, surname, email, regnumber, idcard
                 p = new Person(c.getInt(0), c.getString(1),
                        c.getString(2),c.getString(3),c.getString(4),c.getString(5));
            } while (c.moveToNext());
        }
        c.close();
        dbhelper.close();
        return p;
    }

    @Override
    public void addPerson(Person person) {
        String sql = "INSERT INTO PERSON (NAME, SURNAME, EMAIL, REG_NUMBER, ID_CARD) VALUES('"+person.getName()+
                "','"+person.getSurname()+"','"+person.getEmail()+"','"+person.getRegNumber()+"','"+person.getIdCard()+"')";
        this.dbhelper.executeSQL(sql);
    }

    @Override
    public void removePerson(Person person) {
     this.removePerson(person.getId());
    }

    @Override
    public void removePerson(int ID) {
        String sql = "DELETE FROM PERSON WHERE ID = "+ID;
        this.dbhelper.executeSQL(sql);
    }
}
