package sk.listok.zssk.zssklistok.objects;

/**
 * Cestovný lístok, ktorý mám uložený v mobile.
 */
public class Ticket {
    /**
     * Mesto odkiaľ cestujem
     */
    private String townFrom;

    /**
     * Mesto kamm cestujem
     */
    private String townTo;

    /**
     * Čas kedy bol lístok kúpený
     */
    private String boughtTime;

    public Ticket(String townFrom, String townTo, String boughtTime){
        this.townFrom = townFrom;
        this.townTo = townTo;
        this.boughtTime = boughtTime;
    }

    /**
     * Vytvorí objekt lístka z názvu súboru
     * @param filename názov súboru líska v správnom tvare.
     */
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

    /**
     * Vráti názov súboru.
     * @return
     */
    public String getFilename(){
         return boughtTime+"-"+townFrom+"-"+townTo+"-listok.png";
    }


    @Override
    public String toString() {
        return this.townFrom + " -> "+this.townTo + "\n" + ""+timestampToString(this.boughtTime);
    }


    /**
     * Z časovej pečiaky v názve súboru naformátujem pekne čas.
     * @param timestamp
     * @return
     */
    private String timestampToString(String timestamp){
        String year = timestamp.substring(0,4);
        String month = timestamp.substring(4,6);
        String day = timestamp.substring(6,8);
        String hour = timestamp.substring(8,10);
        String minute = timestamp.substring(10,12);
        String sec = timestamp.substring(12,14);
        return day + "."+month+"."+year +"  ("+hour+":"+minute+":"+sec +")";
    }
}
