package sk.listok.zssk.zssklistok.helpers;

import android.net.Uri;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;

import sk.listok.zssk.zssklistok.PersonInfo;
import sk.listok.zssk.zssklistok.helpers.TrainForParser;

/**
 * Created by Nexi on 24.03.2017.
 */

public class PostDataCreator {


    /**
     * Prva stranka
     * @param train
     * @return
     */
    public static String postFindTrains(TrainForParser train){
        String townFrom = Uri.encode(train.getTownFrom());//"Tepli%C4%8Dka%20nad%20Horn%C3%A1dom";//"%C5%BDilina";
        String townTo = Uri.encode(train.getTownTo()); //"Zvolen";
        String time = Uri.encode(train.getTime()); //18%3A41
        String date = Uri.encode(train.getDate()); //4.3.2017
        return "lang=sk&portal=&from="+townFrom+"&to="+townTo+"&via=&date="+date+"&time="+time+"&departure=true";
    }


    /**
     *  Druha stranka.
     * @param html
     * @param idJourney
     * @return
     */
    public static String postSelectTrain(String html, String idJourney){
        try {
            Document doc = Jsoup.parse(html);

            Elements btns = doc.select(".tmp-buy-btns");
            Elements links = btns.select(".tmp-btn-buy");

            String selectedLink = links.last().attr("onClick");

         /*   int start = selectedLink.indexOf("searchForm:inetConnection");
            int end = selectedLink.indexOf('\'',start);
            int count = end  - start;
            String fi = selectedLink.substring(start,end);
         */
            String fi = idJourney;//this.journeyData.get(0).getIdJourney();

            Element javaView = doc.getElementById("javax.faces.ViewState");
            String value = javaView.attr("value");
            System.out.println("+++++++++++++++"+ value +"+++++++++++++++++++");

            String finalstr = "searchForm=searchForm&"+
                    "javax.faces.ViewState=" + URLEncoder.encode(value, "utf-8") +"&" +
                    URLEncoder.encode(fi,"utf-8")+"="+URLEncoder.encode(fi,"utf-8");
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";

    }

    /**
     * Tretia stranka - vyber typu liska (studentsky, cely,...).
     *
     * @param html
     * @param selectedItemIndex - je index typu - pozri post request.
     * @return
     */
    public static String postTicketType(String html,int selectedItemIndex){
        try {
            Document doc = Jsoup.parse(html);


            Element ticketPassenger = doc.getElementsByAttributeValueContaining("name","ticketParam:passenger:").first();
            String tiPass = ticketPassenger.attr("name");


            Element btn = doc.getElementsContainingOwnText("Rýchly nákup").first();
            String selectedLink = btn.attr("onClick");
            int start = selectedLink.indexOf("ticketParam:");
            int end = selectedLink.indexOf('\\',start);
            int count = end  - start;
            String tiParam = selectedLink.substring(start,end);

            Element javaView = doc.getElementById("javax.faces.ViewState");
            String value = javaView.attr("value");

            System.out.println("+++++++++++++++"+ value +"+++++++++++++++++++");

            String finalstr = "ticketParam=ticketParam&"+
                    URLEncoder.encode(tiPass,"utf-8") + "=" + ""+ selectedItemIndex+"&" +        //cislo podla indexu vybratej polozky v comboboxe
                    URLEncoder.encode("ticketParam:passenger:0:contingentCheck","utf-8") + "=on&"+   // BEZPLATNE IBA PRI STUDENTOVI
                    "javax.faces.ViewState"+"="+URLEncoder.encode(value,"utf-8")+"&"+
                    URLEncoder.encode(tiParam,"utf-8") + "="+URLEncoder.encode(tiParam,"utf-8");
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";

    }

    public static String postPassengerInfo(String html, PersonInfo person){
        try {
            //createPerson(); // nacitam data z editboxov
            Document doc = Jsoup.parse(html);


            Element btn = doc.getElementsContainingOwnText("Pokračovať v platbe").last();
            String selectedLink = btn.attr("onClick");
            int start = selectedLink.indexOf("personalData:");
            int end = selectedLink.indexOf('\'',start);
            int count = end  - start;
            String personalData = selectedLink.substring(start,end);

            Element javaView = doc.getElementById("javax.faces.ViewState");
            String value = javaView.attr("value");

            System.out.println("+++++++++++++++"+ value +"+++++++++++++++++++");

            String finalstr = "ticketParam=ticketParam&"+
                    "personalData=personalData&"+
                    Uri.encode("personalData:payerItemsList:0:field")+ "=" + Uri.encode(person.getEmail())+"&"+
                    Uri.encode("personalData:shoppingCartItemList:0:travellerItemsList:0:field")+ "=" + Uri.encode(person.getName())+"&"+
                    Uri.encode("personalData:shoppingCartItemList:0:travellerItemsList:1:fieldRegId")+ "=" + Uri.encode(person.getSurname())+"&"+
                    Uri.encode("personalData:shoppingCartItemList:0:travellerItemsList:2:field")+ "=" + Uri.encode(person.getIDcard())+"&"+
                    Uri.encode("personalData:shoppingCartItemList:0:travellerItemsList:3:fieldRegId")+ "=" + Uri.encode(person.getRegNumber())+"&"+
                    Uri.encode("personalData:personalDataAgreement")+ "=" + "on&"+                  //suhlasim s podmienkami
                    "javax.faces.ViewState"+"="+Uri.encode(value)+"&"+
                    Uri.encode(personalData,"utf-8") + "="+Uri.encode(personalData,"utf-8");        //ticket data
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";

    }

}
