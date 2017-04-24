package sk.listok.zssk.zssklistok.datalayer;

import android.content.Context;

import sk.listok.zssk.zssklistok.datalayer.objects.Worker;

/**
 * Zastrešuje prácu sa databázou.
 */
public class DatabaseProvider {


    private static DatabaseProvider inst;
    private static Context context;
    private static Worker worker;

    public static DatabaseProvider Instance(Context con) {
        if (inst == null) {
            inst = new DatabaseProvider();
        }
        if (DatabaseProvider.context != con) {
            DatabaseProvider.context = con;
            DatabaseProvider.worker = new Worker(new DatabaseHelper(con));
        }
        return inst;
    }

    private DatabaseProvider() {
        //singleton
    }

    public Worker worker() {
        return worker;
    }
}
