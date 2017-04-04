package sk.listok.zssk.zssklistok.objects;

/**
 * Created by Nexi on 28.02.2017.
 */

public class TrainData {

    private String fromTown;
    private String toTown;
    private String departueDate;
    private String arrivalDate;
    private String departueTime;
    private String arrivalTime;
    private String journeyTime;
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
