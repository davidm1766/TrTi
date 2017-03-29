package sk.listok.zssk.zssklistok.trash;

import android.os.AsyncTask;
import android.view.SearchEvent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import javax.xml.transform.Result;

/**
 * Created by Nexi on 27.02.2017.
 */

/*
public class POSTData extends AsyncTask<INotifiable, Integer, String>   {

    private INotifiable ht;
    private String paHTML;


    private static POSTData inst;

    public static POSTData getInstance(){
        if(POSTData.inst == null){
            inst = new POSTData();
        }
        return inst;
    }

    private POSTData(){

    }

    public static void destroy(){
        try {
            inst = null;
        }catch (Throwable e){
            //ups
            System.out.println(e.toString());
        }

    }

    public String POSTdata(HttpObject ht) {


        try {

            URL url = new URL(ht.getPaUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            connection.setUseCaches(false);

            connection.setRequestProperty("Cookie", ht.getCookiesForConnection());

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
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
            paHTML = html;

        } catch (Exception e) {
            System.out.println("CHYBAA!!!");
            e.printStackTrace();
            paHTML = "";
        }

        return paHTML;
    }



    @Override
    protected String doInBackground(INotifiable... ht) {
        this.ht = ht[0];
        return POSTdata(this.ht.getHt());
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
       // setProgressPercent(progress[0]);
    }

    static String res;
    @Override
    protected void onPostExecute(String result) {
            ht.notify(res);
    }


}
*/