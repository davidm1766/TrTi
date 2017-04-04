package sk.listok.zssk.zssklistok.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import sk.listok.zssk.zssklistok.communication.DataHolder;

/**
 * Created by Nexi on 02.04.2017.
 */

public class ImageHelper extends AsyncTask<DataHolder,Void,Bitmap> {



    private byte[] downloadTicketImage(DataHolder ht){
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
     * @param arr
     */
    private void saveImageToFile(byte[] arr){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/vlakoveListky");
        myDir.mkdirs();
        //timestamp
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = s.format(new Date());

        File file = new File(myDir,"listok"+format+".png");
        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(arr);
            fos.close();
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }


    @Override
    protected Bitmap doInBackground(DataHolder... params) {
        //najprv si stiahnem listok v .png
        byte[] arr = downloadTicketImage(params[0]);
        //potom ho ulozim do suboru
        saveImageToFile(arr);
        return BitmapFactory.decodeByteArray(arr, 0, arr.length);
    }
}
