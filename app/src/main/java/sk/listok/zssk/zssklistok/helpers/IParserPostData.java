package sk.listok.zssk.zssklistok.helpers;

import java.lang.reflect.InvocationTargetException;



/**
 *   Tento interface sluzi len na zobrazenie funkcii, ktore obsahuje skopilovany PostCreator.
 */
public interface IParserPostData {

    String postFindTrains(String sTownFrom, String sTownTo, String sTime, String sDate) throws InvocationTargetException, IllegalAccessException;
    String postSelectTrain(String html, String idJourney);
    String postTicketType(String html,int selectedItemIndex);
    String postPassengerInfo(String html, String mail, String name, String surname, String regNumber);
    String postFinishPayment(String html);
    String postDownloadTicket(String html);

}
