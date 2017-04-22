package sk.listok.zssk.zssklistok.classloader;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nexi on 22.04.2017.
 */

public class DexCheckVersion extends AsyncTask<Void,Void,String> {

    private INotifyDownloadVersionDex inot;

    public DexCheckVersion(INotifyDownloadVersionDex inot){
        this.inot = inot;
    }

    @Override
    protected String doInBackground(Void... params) {

        try {

            URL url = new URL("http://trainapi.azurewebsites.net/api/checkVersion");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            return builder.toString().trim().split("\"")[1];

        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        inot.Downloaded(s);
    }
}
