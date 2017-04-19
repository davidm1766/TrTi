package sk.listok.zssk.zssklistok.classloader;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.Permissions;
import com.microsoft.azure.storage.SharedAccessAccountPermissions;
import com.microsoft.azure.storage.SharedAccessAccountPolicy;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.microsoft.azure.storage.file.FileInputStream;

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
 * Created by Nexi on 10.04.2017.
 */

public class DexDownloader {


    public DexDownloader(){

            new Thread(new Runnable() {
                @Override
                public void run() {
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




                        File myDir = FileHelper.getTicketFolder();
                        File file = new File(myDir,"final.dex");
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(output.toByteArray());
                        fos.close();

                    }catch (Exception e){
                        e.printStackTrace();

                    }
                }
            }).start();
    }

}
