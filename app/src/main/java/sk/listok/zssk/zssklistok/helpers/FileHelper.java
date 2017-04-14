package sk.listok.zssk.zssklistok.helpers;

import android.os.Environment;

import java.io.File;

/**
 * Created by Nexi on 12.04.2017.
 */

public class FileHelper {

    public static File getTicketFolder(){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/vlakoveListky");
        myDir.mkdirs();
        return myDir;
    }

    public static File getTicketImage(String filename){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/vlakoveListky/"+filename);
        myDir.mkdirs();
        return myDir;
    }



}
