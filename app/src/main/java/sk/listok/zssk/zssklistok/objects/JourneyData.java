package sk.listok.zssk.zssklistok.objects;

import java.util.ArrayList;

/**
 * Informácie o ceste z mesta do mesta, môže to
 * byť aj na viac prestupov, tieto sú v Liste s TrainData
 */
public class JourneyData {

    /**
     * Zoznam všetkých vlakov (prestupov)
     * na ceste z mesta do mesta.
     */
    private ArrayList<TrainData> trainData;

    /**
     * ID cesty získane zo stránky.
     */
    private String idJourney;

    /**
     * Celkový čas cesty aj s prestupmi.
     */
    private String journeyTime;


    public JourneyData(){
        this.trainData = new ArrayList<>();
    }


    public String getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(String journeyTime) {
        this.journeyTime = journeyTime;
    }

    public void setIdJourney(String idJourney) {
        this.idJourney = idJourney;
    }

    public String getIdJourney() {
        return idJourney;
    }

    public ArrayList<TrainData> getTrainData() {
        return trainData;
    }

    public void setTrainData(ArrayList<TrainData> trainData) {
        this.trainData = trainData;
    }
}

