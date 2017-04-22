package sk.listok.zssk.zssklistok.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.URLEncoder;



/**
 *  Nepouzivat (len kvoli editacii je v projekte), samostatny .dex.
 */
public class PostCreator implements IParserPostData{



    public PostCreator(){

    }
    /**
     * Prva stranka
     * @return
     */
    public String postFindTrains(String sTownFrom, String sTownTo, String sTime, String sDate){
        try {
            String townFrom = URLEncoder.encode(sTownFrom, "utf-8");//"Tepli%C4%8Dka%20nad%20Horn%C3%A1dom";//"%C5%BDilina";
            String townTo = URLEncoder.encode(sTownTo, "utf-8"); //"Zvolen";
            String time = URLEncoder.encode(sTime, "utf-8"); //18%3A41
            String date = URLEncoder.encode(sDate, "utf-8"); //4.3.2017
            return "lang=sk&portal=&from="+townFrom+"&to="+townTo+"&via=&date="+date+"&time="+time+"&departure=true";
        } catch (Exception ex){
            System.out.println(ex.toString());
            return "";
        }

    }


    /**
     *  Druha stranka.
     * @param html
     * @param idJourney
     * @return
     */
    public String postSelectTrain(String html, String idJourney){
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
    public String postTicketType(String html,int selectedItemIndex){
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

    public String postPassengerInfo(String html, String mail, String name, String surname, String regNumber){
        try {
            //createPerson(); // nacitam data z editboxov
            Document doc = Jsoup.parse(html);


            Element btn = doc.getElementsContainingOwnText("Pokračovať v platbe").last();
            String selectedLink = btn.attr("onClick");
            int start = selectedLink.indexOf("personalData:");
            int end = selectedLink.indexOf('\'',start);
            String personalData = selectedLink.substring(start,end);

            Element javaView = doc.getElementById("javax.faces.ViewState");
            String value = javaView.attr("value");

            System.out.println("+++++++++++++++"+ value +"+++++++++++++++++++");

            String finalstr = "ticketParam=ticketParam&"+
                    "personalData=personalData&"+
                    URLEncoder.encode("personalData:payerItemsList:0:field","utf-8")+ "=" + URLEncoder.encode(mail, "utf-8")+"&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:0:field", "utf-8")+ "=" + URLEncoder.encode(name, "utf-8")+"&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:1:fieldRegId", "utf-8")+ "=" + URLEncoder.encode(surname, "utf-8")+"&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:2:fieldRegId", "utf-8")+ "=" + URLEncoder.encode(regNumber, "utf-8")+"&"+
                    //Uri.encode("personalData:shoppingCartItemList:0:travellerItemsList:2:field")+ "=" + Uri.encode(person.getIDcard())+"&"+
                    //Uri.encode("personalData:shoppingCartItemList:0:travellerItemsList:3:fieldRegId")+ "=" + Uri.encode(person.getRegNumber())+"&"+
                    //Uri.encode("personalData:personalDataAgreement")+ "=" + "on&"+                  //suhlasim s podmienkami
                    "javax.faces.ViewState"+"="+URLEncoder.encode(value, "utf-8")+"&"+
                    URLEncoder.encode(personalData,"utf-8") + "="+URLEncoder.encode(personalData,"utf-8");        //ticket data
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";

    }


    public String postFinishPayment(String html){

        try {
            Document doc = Jsoup.parse(html);


            Element btn = doc.getElementsContainingOwnText("Pokračovať v nákupe").last();
            String selectedLink = btn.attr("onClick");
            int start = selectedLink.indexOf("paymentForm:");
            int end = selectedLink.indexOf('\\',start);
            int count = end  - start;
            String paymentForm = selectedLink.substring(start,end);

            Element javaView = doc.getElementById("javax.faces.ViewState");
            String value = javaView.attr("value");

            System.out.println("+++++++++++++++"+ value +"+++++++++++++++++++");

            String finalstr =
                    "paymentForm=paymentForm&"+
                            URLEncoder.encode("paymentForm:SendConfirmation","utf-8")+ "=" + URLEncoder.encode("on","utf-8")+"&"+
                            URLEncoder.encode("paymentForm:PersonalDataFormDirect","utf-8")+ "=" + URLEncoder.encode("on","utf-8")+"&"+
                            "javax.faces.ViewState"+"="+URLEncoder.encode(value,"utf-8")+"&"+
                            URLEncoder.encode(paymentForm,"utf-8") + "="+URLEncoder.encode(paymentForm,"utf-8");
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";
    }

    public String postDownloadTicket(String html){

        try {
            Document doc = Jsoup.parse(html);
            Element javaView = doc.getElementById("javax.faces.ViewState");
            String value = javaView.attr("value");

            Element btnDownload = doc.getElementsContainingOwnText("Stiahnuť lístok pre mobil").last();
            String selectedLink = btnDownload.attr("onClick");
            int start = selectedLink.indexOf("travelDocument:");
            int end = selectedLink.indexOf('\'',start);
            String traveldoc = selectedLink.substring(start,end);

            String finalstr =
                    "travelDocument=travelDocument&"+
                            "javax.faces.ViewState"+"="+URLEncoder.encode(value,"utf-8")+"&"+
                            URLEncoder.encode(traveldoc,"utf-8") + "="+URLEncoder.encode(traveldoc,"utf-8");
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());
        }
        return "";
    }

    public String checkNoMoreTickets(String html){
        try{
            Document doc = Jsoup.parse(html);
            Elements notickets = doc.select(".tmp-anulation-form-false");
            if(notickets == null || notickets.size() == 0){
                return "";
            } else {
                return notickets.first().html();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }


    public String ticketPrice(String html){
        Document doc = Jsoup.parse(html);
        return doc.getElementById("personalData:priceContainer").select("div > p > strong").html();
    }


    public String ticketDetails(String html){
        Document doc = Jsoup.parse(html);
        Elements detailData =doc.select(".tmp-shopping-detail-data");
        Elements journeyDetail = detailData.select("div > ul");
        return journeyDetail.first().children().html();

    }
}
