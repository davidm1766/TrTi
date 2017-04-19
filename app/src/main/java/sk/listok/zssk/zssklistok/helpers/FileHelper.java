package sk.listok.zssk.zssklistok.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Nexi on 12.04.2017.
 */

public class FileHelper {

    private static Context context;

    public static File getTicketFolder(){
        String root = Environment.getExternalStorageDirectory().toString();//context.getFilesDir().toString();
        //File file = new File(context.getFilesDir());
        File myDir = new File(root + "/vlakoveListky");
        myDir.mkdirs();
        return myDir;
    }

    public static File getTempFolder(){
        String root = context.getCacheDir().toString();
        File myDir = new File(root + "/tmp");
        myDir.mkdirs();
        return myDir;
    }

    public static File getAssetsFolder(String filename){
        return new File(context.getExternalFilesDir(null), filename);
    }


    public static File getTicketImage(String filename){
        String root =  Environment.getExternalStorageDirectory().toString();//context.getFilesDir().toString(); //Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/vlakoveListky/"+filename);
        myDir.mkdirs();
        return myDir;
    }

    public static void copyAssets(Context context) {
        FileHelper.context = context;

        //pozriem sa ci v tempe uz nemam
        //nakopirovane subory aby som ich neprevalil
        File tempDir = FileHelper.getTempFolder();
        ArrayList<String> filesInTemp = new ArrayList<>();
        for (File f : tempDir.listFiles()) {
            if (f.isFile()) {
                filesInTemp.add(f.getName());
            }
        }

        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

        // ak nemam ziadne subory koncim...
        if(files == null){
            return;
        }

        for (String filename : files) {
            if(filesInTemp.contains(filename)){
                continue;
            }
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(FileHelper.getTempFolder(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


}
