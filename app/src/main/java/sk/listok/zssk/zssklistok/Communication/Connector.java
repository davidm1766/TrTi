package sk.listok.zssk.zssklistok.communication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Connector komunikuje priamo so stránkou železníc. Posiela POST požiadavky
 * a v DataHolderi vracia celé HTML. Zo vstupného DataHoldera sa vyberie URL
 * adresa, cookies, POST dáta a odošlu sa. Response z tejto požiadavky sa uloži
 * do DataHoldera a vráti sa naspäť.
 */

public class Connector extends AsyncTask<DataHolder, Void, DataHolder> {


    private INotifyDownloader inotify;

    public Connector(INotifyDownloader in) {
        if (android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        if (in == null) {
            throw new IllegalArgumentException("INotifyDownloader is null!");
        }
        inotify = in;
    }

    /**
     * Samotné odoslanie požiadavky na server.
     *
     * @param ht z dataholdera sa vyberú všetky potrebné
     *           informácie na odoslanie POST requestu.
     * @return
     */
    private DataHolder requestToServer(DataHolder ht) {
        HttpURLConnection connection = null;
        try {

            URL url = new URL(ht.getPaUrl());
            connection = (HttpURLConnection) url.openConnection();
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
            //nastavim nove html
            ht.setPaHtml(html);

        } catch (Exception e) {
            System.out.println("CHYBAA!!!");
            e.printStackTrace();
            ht.setPaHtml("");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return ht;
    }

    @Override
    protected DataHolder doInBackground(DataHolder... params) {
        DataHolder dh = params[0];
        requestToServer(dh);
        return dh;
    }


    @Override
    protected void onPostExecute(DataHolder result) {
        // po dokonceni vysledky...
        inotify.downloaded(result);
    }


}
