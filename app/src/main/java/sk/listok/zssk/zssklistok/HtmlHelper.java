package sk.listok.zssk.zssklistok;

import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Nexi on 22.02.2017.
 */




public class HtmlHelper  {

    HttpObject ht;
    public HtmlHelper(HttpObject ht){
       this.ht = ht;
    }


    public void downloadPage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runProgram();
                } catch (Exception e) {
                    System.out.println("CHYBAA!!!");
                    e.printStackTrace();
                } finally {

                }
            }
        }).start();
    }

    public void runProgram(){
        //Select towns
        ht.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/connection/portal.xhtml");
        ht.setPaPOSTdata("lang=sk&portal=&from=Zvolen&to=%C5%BDilina&via=&date=27.2.2017&time=4%3A41&departure=true");


        String html = POSTdata(ht);

        //SelectTrain
        String resp = parser(html);
        ht.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/connection/search.xhtml");
        ht.setPaPOSTdata(resp);
        String second = POSTdata(ht);

        //SelectPassengetTYpe
        String postData = parser2(second);
        ht.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/ticketVCD.xhtml");
        ht.setPaPOSTdata(postData);

        //selectPassengerInfo
        String third = POSTdata(ht);
        String postData2 = parser3(third);
        System.out.println("POST:"+postData2);
            ht.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/personalData.xhtml");
        ht.setPaPOSTdata(postData2);


        String fourht = POSTdata(ht);


        //kupenie listka ziatial vynecham
       /*
        String postData3 = parser4(fourht);
        System.out.println("POST:"+postData3);
        ht.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/payment.xhtml");
        ht.setPaPOSTdata(postData3);
        ht.setPaCookie(false);


        String fifth = POSTdata(ht);
        System.out.println(fifth);
        */
    }


    public String POSTdata(HttpObject ht){
        try{

            URL url = new URL(ht.getPaUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            connection.setUseCaches (false);

            connection.setRequestProperty("Cookie", ht.getCookiesForConnection());

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.writeBytes(ht.getPaPOSTdata());
            wr.flush();
            wr.close();


            ht.setCookies(connection.getHeaderFields());


            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            String html = builder.toString();
            return html;

        }catch(Exception e){
            System.out.println("ERROR"+e.toString());
        }
        return null;
    }

    public static String parser(String html)
    {
        try {
            Document doc = Jsoup.parse(html);

            Elements btns = doc.select(".tmp-buy-btns");
            Elements links = btns.select(".tmp-btn-buy");

            String selectedLink = links.last().attr("onClick");

            int start = selectedLink.indexOf("searchForm:inetConnection");
            int end = selectedLink.indexOf('\'',start);
            int count = end  - start;
            String fi = selectedLink.substring(start,end);



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

    public static String parser2(String html)
    {
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
                    URLEncoder.encode(tiPass,"utf-8") + "=" + "2&" +
                    URLEncoder.encode("ticketParam:passenger:0:contingentCheck","utf-8") + "=on&"+   // BEZPLATNE IBA PRI STUDENTOVI
                    "javax.faces.ViewState"+"="+URLEncoder.encode(value,"utf-8")+"&"+
                    URLEncoder.encode(tiParam,"utf-8") + "="+URLEncoder.encode(tiParam,"utf-8");
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";
    }

    public static String parser3(String html)
    {
        try {
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
                    URLEncoder.encode("personalData:payerItemsList:0:field","utf-8")+ "=" + URLEncoder.encode("madaras.david1@gmail.com","utf-8")+"&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:0:field","utf-8")+ "=" + URLEncoder.encode("Dávid","utf-8")+"&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:1:fieldRegId","utf-8")+ "=" + "Madaras&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:2:field","utf-8")+ "=" + "EB793051&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:3:fieldRegId","utf-8")+ "=" + "1588524&"+
                    URLEncoder.encode("personalData:personalDataAgreement","utf-8")+ "=" + "on&"+
                    "javax.faces.ViewState"+"="+URLEncoder.encode(value,"utf-8")+"&"+
                    URLEncoder.encode(personalData,"utf-8") + "="+URLEncoder.encode(personalData,"utf-8");
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";
    }


    public static String parser4(String html)
    {
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
}
