package sk.listok.zssk.zssklistok.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by Nexi on 31.03.2017.
 */

public class ParserHelper {


    public static String ticketPrice(String html){
        Document doc = Jsoup.parse(html);
        return doc.getElementById("personalData:priceContainer").select("div > p > strong").html();
    }


    public static String ticketDetails(String html){
        Document doc = Jsoup.parse(html);
        Elements detailData =doc.select(".tmp-shopping-detail-data");
        Elements journeyDetail = detailData.select("div > ul");
        return journeyDetail.first().children().html();

    }

}
