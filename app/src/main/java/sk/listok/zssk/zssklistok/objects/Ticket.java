package sk.listok.zssk.zssklistok.objects;

/**
 * Created by Nexi on 12.04.2017.
 */

public class Ticket {
    private String townFrom;
    private String townTo;
    private String boughtTime;

    public Ticket(String townFrom, String townTo, String boughtTime){
        this.townFrom = townFrom;
        this.townTo = townTo;
        this.boughtTime = boughtTime;
    }

    public Ticket(String filename){
        try{
            String[] splited = filename.split("-");
            this.townFrom = splited[1];
            this.townTo = splited[2];
            this.boughtTime = splited[0];
        }catch (Exception ex){
            throw new IllegalArgumentException();
        }
    }

    public String getBoughtTime() {
        return boughtTime;
    }

    public String getTownFrom() {
        return townFrom;
    }

    public String getTownTo() {
        return townTo;
    }

    public String getFilename(){
         return boughtTime+"-"+townFrom+"-"+townTo;
    }

    @Override
    public String toString() {
        return this.townFrom + " -> "+this.townTo + "\n" + "zakúpený "+timestampToString(this.boughtTime);
    }

    private String timestampToString(String timestamp){
        String year = timestamp.substring(0,2);
        String month = timestamp.substring(2,4);
        String day = timestamp.substring(4,6);
        String hour = timestamp.substring(6,8);
        String minute = timestamp.substring(8,10);
        String sec = timestamp.substring(10,12);
        return day + "."+month+"."+year +"  ("+hour+":"+minute+":"+sec +")";
    }
}
