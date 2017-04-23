package sk.listok.zssk.zssklistok.classloader;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.InvalidKeyException;
import java.sql.Blob;


import dalvik.system.DexClassLoader;
import sk.listok.zssk.zssklistok.helpers.FileHelper;

/**
 * Trieda, ktorá má za úlohu stiahnutie .dex súboru z API.
 */
public class DexDownloader  extends AsyncTask<Void,Void,DexDownloadInfo>{

    private INotifyDownloadDex inot;
    public DexDownloader(INotifyDownloadDex inot){
        this.inot = inot;
    }

    @Override
    protected DexDownloadInfo doInBackground(Void... params) {
        DexDownloadInfo dInfo = new DexDownloadInfo();

        try {

            URL url = new URL("http://trainapi.azurewebsites.net/api/getFile");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = is.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            dInfo.setDexBytes(output.toByteArray());

            dInfo.setStatus(eStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            dInfo.setStatus(eStatus.FAILED);
        }
        return dInfo;
    }


    @Override
    protected void onPostExecute(DexDownloadInfo dexDownloadInfo) {
        inot.DownloadedDex(dexDownloadInfo);
    }
}
