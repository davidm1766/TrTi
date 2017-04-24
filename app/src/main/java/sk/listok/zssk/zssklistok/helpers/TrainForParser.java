package sk.listok.zssk.zssklistok.helpers;


/**
 * Created by Nexi on 24.03.2017.
 */

public class TrainForParser {
    private String townFrom;
    private String townTo;
    private String time;
    private String date;


    public TrainForParser(String townFrom, String townTo, String time, String date) {
        this.townFrom = townFrom;
        this.townTo = townTo;
        this.time = time;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTownFrom() {
        return townFrom;
    }

    public String getTownTo() {
        return townTo;
    }
}
