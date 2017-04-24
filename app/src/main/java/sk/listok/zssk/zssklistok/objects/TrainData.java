package sk.listok.zssk.zssklistok.objects;

/**
 * Informácie o jednom vlaku.
 */
public class TrainData {

    /**
     * Mesto z kadial chcem cestovať.
     */
    private String fromTown;

    /**
     * Mesto kam chcem cestovať.
     */
    private String toTown;

    /**
     * Dátum odchodu vlaku
     */
    private String departueDate;

    /**
     * Dátum príchodu vlaku
     */
    private String arrivalDate;

    /**
     * Čas odchodu vlaku
     */
    private String departueTime;

    /**
     * Čas príchodu vlaku
     */
    private String arrivalTime;

    /**
     * Celkový čas cestovania.
     */
    private String journeyTime;

    /**
     * Názov vlaku
     */
    private String nameTrain;


    public String getNameTrain() {
        return nameTrain;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartueDate() {
        return departueDate;
    }

    public String getDepartueTime() {
        return departueTime;
    }

    public String getFromTown() {
        return fromTown;
    }

    public String getJourneyTime() {
        return journeyTime;
    }

    public String getToTown() {
        return toTown;
    }


    public void setNameTrain(String nameTrain) {this.nameTrain = nameTrain; }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDepartueDate(String departueDate) {
        this.departueDate = departueDate;
    }

    public void setDepartueTime(String departueTime) {
        this.departueTime = departueTime;
    }

    public void setFromTown(String fromTown) {
        this.fromTown = fromTown;
    }

    public void setToTown(String toTown) {
        this.toTown = toTown;
    }

    public void setJourneyTime(String journeyTime) {
        this.journeyTime = journeyTime;
    }

}
