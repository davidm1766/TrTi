package sk.listok.zssk.zssklistok.communication;

import android.text.TextUtils;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

/**
 * Created by Nexi on 04.03.2017.
 */

public class DataHolder {


    private String paUrl;
    private String paPOSTdata;
    private String paHtml;
    private String paCookies;




    private static DataHolder inst;

    public static DataHolder getInst(){
        if(inst == null){
            inst = new DataHolder();
        }
        return inst;
    }

    /**
     * DEPRECATED: len pre testovanie - nepouzivat
     * @return
     */
    public static void setInst(DataHolder dh){
        DataHolder.inst = dh;
    }


    public DataHolder(){
        //singleton
    }


    public String getCookiesForConnection(){
        return this.paCookies;
    }

    public void setCookies(Map<String, List<String>> headerFields){
        CookieManager paCookiemng = new CookieManager();
        String COOKIES_HEADER = "Set-Cookie";
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                paCookiemng.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }
        if (paCookiemng.getCookieStore().getCookies().size() > 0) {
            this.paCookies =  TextUtils.join(";",  paCookiemng.getCookieStore().getCookies());
        }
    }

    public String getPaHtml() {
        return paHtml;
    }

    public String getPaPOSTdata() {
        return paPOSTdata;
    }

    public String getPaUrl() {
        return paUrl;
    }

    public void setPaHtml(String paHtml) {
        this.paHtml = paHtml;
    }

    public void setPaPOSTdata(String paPOSTdata) {
        this.paPOSTdata = paPOSTdata;
    }

    public void setPaUrl(String paUrl) {
        this.paUrl = paUrl;
    }

}