package sk.listok.zssk.zssklistok.helpers;

import android.support.v7.app.AppCompatActivity;

import sk.listok.zssk.zssklistok.classloader.ClassProvider;
import sk.listok.zssk.zssklistok.datalayer.objects.Person;


/**
 * Created by Nexi on 10.04.2017.
 */

public class PostDataCreatorDynamic implements IParserPostData {

    private AppCompatActivity activity;

    private static PostDataCreatorDynamic inst;
    public static PostDataCreatorDynamic Instance(AppCompatActivity act){
        if(inst == null){
            inst = new PostDataCreatorDynamic(act);
        }
        // ak sa zmenila aktivita aktualizujem to...
        if(inst.activity != act){
            inst.activity = act;
        }

        return inst;
    }

    private PostDataCreatorDynamic(){
        //singleton
    }


    public PostDataCreatorDynamic(AppCompatActivity activity){
        this.activity = activity;
    }


    public String postFindTrains(TrainForParser train) {
        return this.postFindTrains(train.getTownFrom(),train.getTownTo(),train.getTime(),train.getDate());
    }
    @Override
    public String postFindTrains(String sTownFrom, String sTownTo, String sTime, String sDate) {
        Object ret = ClassProvider.Instance(activity).getMethodResult("postFindTrains",new Class[]
                {String.class,String.class,String.class,String.class},sTownFrom,sTownTo,sTime,sDate);
        if(ret == null){
            return null;
        }else {
            return (String)ret;
        }

    }

    @Override
    public String postSelectTrain(String html, String idJourney) {
        Object ret = ClassProvider.Instance(activity).getMethodResult("postSelectTrain",new Class[]{String.class,String.class},html,idJourney);
        if(ret == null){
            return null;
        }else {
            return (String)ret;
        }
    }

    @Override
    public String postTicketType(String html, int selectedItemIndex) {
        Object ret = ClassProvider.Instance(activity).getMethodResult("postTicketType",new Class[]{String.class,int.class},html,selectedItemIndex);
        if(ret == null){
            return null;
        }else {
            return (String)ret;
        }

    }

    public String postPassengerInfo(String html, Person person) {
        return this.postPassengerInfo(html,person.getEmail(),person.getName(),person.getSurname(),person.getRegNumber());
    }

    @Override
    public String postPassengerInfo(String html, String mail, String name, String surname, String regNumber) {
        Object ret = ClassProvider.Instance(activity).getMethodResult("postPassengerInfo",
                new Class[]{String.class, String.class,String.class,String.class,String.class},
                html,mail,name,surname,regNumber);
        if(ret == null){
            return null;
        }else {
            return (String)ret;
        }
    }

    @Override
    public String postFinishPayment(String html) {
        Object ret = ClassProvider.Instance(activity).getMethodResult("postFinishPayment",
                new Class[]{String.class},html);
        if(ret == null){
            return null;
        }else {
            return (String)ret;
        }
    }

    @Override
    public String postDownloadTicket(String html) {
        Object ret = ClassProvider.Instance(activity).getMethodResult("postDownloadTicket",
                new Class[]{String.class},html);
        if(ret == null){
            return null;
        }else {
            return (String)ret;
        }
    }

    @Override
    public String checkNoMoreTickets(String html) {
        Object ret = ClassProvider.Instance(activity).getMethodResult("checkNoMoreTickets",
                new Class[]{String.class},html);
        if(ret == null){
            return null;
        }else {
            return (String)ret;
        }
    }

}

