
package sk.listok.zssk.zssklistok;

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
/**
 * Created by Nexi on 04.03.2017.
 */

public class POSTDataSync {


        private static DataHolder ht;
        private static String paHTML;


        public String POSTDataSyncFunc(DataHolder ht1) {

            POSTDataSync.ht = ht1;
            Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

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
                POSTDataSync.paHTML = html;

            } catch (Exception e) {
                System.out.println("CHYBAA!!!");
                e.printStackTrace();
                POSTDataSync.paHTML = "";
            }
            }
       });
        try {
            t.start();
            t.join();
        }catch (Exception e){
        }

            return POSTDataSync.paHTML;
        }


}
