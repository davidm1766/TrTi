package sk.listok.zssk.zssklistok.dataLayer;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Nexi on 20.03.2017.
 */

@DatabaseTable(tableName = "TOWN")
public class Town {

    @DatabaseField//(columnName = "ID")
    private int ID;

    @DatabaseField//(columnName = "NAME")
    private String NAME;
}
