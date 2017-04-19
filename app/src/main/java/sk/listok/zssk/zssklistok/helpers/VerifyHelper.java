package sk.listok.zssk.zssklistok.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by Nexi on 19.04.2017.
 */

public class VerifyHelper {

    public static String checkUserInfo(String html){
        try {
            Document doc = Jsoup.parse(html);

            Elements errors = doc.select(".tmp-anulation-form-false");
            if (errors != null && errors.size() != 0) {
                return errors.first().html();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";

    }

}
