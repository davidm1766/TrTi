package sk.listok.zssk.zssklistok;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.AppLaunchChecker;
import android.text.TextUtils;

import java.io.Serializable;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

/**
 * Created by Nexi on 25.02.2017.
 */
import android.app.Application;





public class HttpObject extends Application {

    private String paUrl;
    private String paPOSTdata;
    private String paHtml;
    private String paCookies;



    public HttpObject(Parcel in){
        String[] data = new String[4];
        in.readStringArray(data);
        this.paUrl = data[0];
        this.paPOSTdata = data[1];
        this.paCookies = data[2];
        this.paHtml = data[3];

    }

    public HttpObject() {}

    public HttpObject(String url, String POSTdata){
        paUrl = url;
        paPOSTdata = POSTdata;
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
