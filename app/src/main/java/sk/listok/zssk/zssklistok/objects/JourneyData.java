package sk.listok.zssk.zssklistok.objects;

import java.util.ArrayList;

/**
 * Created by Nexi on 02.03.2017.
 */

public class JourneyData {

    private ArrayList<TrainData> trainData;
    private String idJourney;
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

