package sk.listok.zssk.zssklistok.datalayer.objects;


/**
 * Created by Nexi on 20.03.2017.
 */

public class Town {
    private int id;
    private String name;

    public Town(String name, int ID) {
        this.id = ID;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

