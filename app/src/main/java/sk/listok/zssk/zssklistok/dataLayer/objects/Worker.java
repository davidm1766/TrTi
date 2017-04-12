package sk.listok.zssk.zssklistok.dataLayer.objects;

import java.net.PortUnreachableException;

import sk.listok.zssk.zssklistok.dataLayer.DatabaseHelper;

/**
 * Created by Nexi on 11.04.2017.
 */

public class Worker {

    private IQueryPerson iperson;
    private IQueryTown itown;
    private DatabaseHelper dbhelper;

    public Worker(IQueryPerson ip, IQueryTown it){
        this.iperson = ip;
        this.itown = it;
    }

    public Worker(DatabaseHelper dbhelper){
        this.dbhelper = dbhelper;
        this.itown = new QueryTown(dbhelper);
        this.iperson = new QueryPerson(dbhelper);
    }

    public IQueryPerson person() {
        return iperson;
    }

    public IQueryTown towns() {
        return itown;
    }

}
