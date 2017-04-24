package sk.listok.zssk.zssklistok.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import sk.listok.zssk.zssklistok.objects.Ticket;

/**
 * Pomocník na prácu so súbormi.
 */
public class FileHelper {

    public static final String DATABASE_PATH = "/data/data/sk.listok.zssk.zssklistok/databases/";
    public static final String DATABASE_NAME = "database";
    private static Context context;
    private static String dexFileName = "parser.dex";


    /**
     * Vráti priečinok kde sú uložené stiahnuté lístky
     */
    public static File getTicketFolder(){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/vlakoveListky");
        myDir.mkdirs();
        return myDir;
    }

    /**
     * Vráti umiestnenie temp foldera.
     */
    public static File getTempFolder(){
        String root = context.getCacheDir().toString();
        File myDir = new File(root + "/tmp");
        myDir.mkdirs();
        return myDir;
    }


    /**
     * Vráti .dex súbor(bajtkód) parsera
     */
    public static File getDexFile(){
        File dexFile = new File(context.getFilesDir() + "/dexfile/"+dexFileName);
        dexFile.mkdirs();
        return dexFile;
    }

    /**
     * Vráti adresár kde sa .dex nachádza
     */
    public static File getDexDir(){
        File dexDir = new File(context.getFilesDir() + "/dexfile");
        dexDir.mkdirs();
        return dexDir;
    }


    /**
     * Prepíše existujúci .dex súbor, novým bajtkódom.
     */
    public static void rewriteToDexFile(byte[] bytes){
        try {
            File f = getDexFile();
            f.delete();

            File file = new File(getDexDir(), dexFileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Vráti súbor lístka podľa mena.
     */
    public static File getTicketImage(String filename){
        String root =  Environment.getExternalStorageDirectory().toString();//context.getFilesDir().toString(); //Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/vlakoveListky/"+filename);
        return myDir;
    }

    /**
     * Skopíruje assets.
     */
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

    /**
     * Skopíruje súbor z - do
     */
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    /**
     * Vymaže lístok z fyzickej pamäte.
     */
    public static boolean deleteTicket(Ticket ticket) {
        File f = getTicketImage(ticket.getFilename());
        if(f != null){
            f.delete();
            return true;
        }else{
            return false;
        }
    }

    /**
     * Vráti priečinok kde sa môže bajtkód nakopírovať, pri loade.
     */
    public static File getOutputDex(Context mActivity) {
        return mActivity.getDir("dex", Context.MODE_PRIVATE);
    }
}
