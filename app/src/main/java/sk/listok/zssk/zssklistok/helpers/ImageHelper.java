package sk.listok.zssk.zssklistok.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sk.listok.zssk.zssklistok.communication.DataHolder;
import sk.listok.zssk.zssklistok.communication.INotifyImageDownloaded;
import sk.listok.zssk.zssklistok.objects.Ticket;

/**
 * Helper na stiahnutie obrázku lístka.
 */

public class ImageHelper extends AsyncTask<Pair<DataHolder, Ticket>, Void, Bitmap> {


    private INotifyImageDownloaded inotify;

    public ImageHelper(INotifyImageDownloaded inot) {
        if (inot == null) {
            throw new IllegalArgumentException();
        }
        this.inotify = inot;
    }

    private byte[] downloadTicketImage(DataHolder ht) {
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

            // ht.setCookies(connection.getHeaderFields());


            InputStream is = connection.getInputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = is.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }


            return output.toByteArray();
        } catch (Exception e) {
            System.out.println("CHYBAA!!!");
            e.printStackTrace();
            ht.setPaHtml("");
        }
        return new byte[0];
    }

    /**
     * Ulozi pole bajtov predstavujuce obrazok
     *
     * @param arr
     */
    private void saveImageToFile(byte[] arr, Ticket ticket) {
        File myDir = FileHelper.getTicketFolder(inotify.getCompatActivity());
        File file = new File(myDir, ticket.getFilename());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(arr);
            fos.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


    @Override
    protected Bitmap doInBackground(Pair<DataHolder, Ticket>... params) {
        //najprv si stiahnem listok v .png
        byte[] arr = downloadTicketImage(params[0].first);
        //potom ho ulozim do suboru
        saveImageToFile(arr, params[0].second);
        return BitmapFactory.decodeByteArray(arr, 0, arr.length);
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        inotify.imageDownloaded(bitmap);
    }
}
