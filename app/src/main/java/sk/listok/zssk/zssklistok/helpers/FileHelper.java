package sk.listok.zssk.zssklistok.helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
     * Kontrola pre Android 6+, či sú práva na zápis.
      */
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static void checkWriteExternalPermission(AppCompatActivity app){
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(app, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(app,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(app,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);

                }
            }
        }
    }


    /**
     * Vráti priečinok kde sú uložené stiahnuté lístky
     */
    public static File getTicketFolder(AppCompatActivity app) {
        checkWriteExternalPermission(app);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/vlakoveListky");
        myDir.mkdirs();
        return myDir;
    }

    /**
     * Vráti umiestnenie temp foldera.
     */
    private static File getTempFolder(AppCompatActivity app) {
        String root = context.getCacheDir().toString();
        File myDir = new File(root + "/tmp");
        myDir.mkdirs();
        return myDir;
    }


    /**
     * Vráti .dex súbor(bajtkód) parsera
     */
    public static File getDexFile(AppCompatActivity app) {
        checkWriteExternalPermission(app);
        File dexFile = new File(context.getFilesDir() + "/dexfile/" + dexFileName);
        return dexFile;
    }

    /**
     * Vráti adresár kde sa .dex nachádza
     */
    public static File getDexDir(AppCompatActivity app) {
        checkWriteExternalPermission(app);
        File dexDir = new File(context.getFilesDir() + "/dexfile");
        dexDir.mkdirs();
        return dexDir;
    }


    /**
     * Prepíše existujúci .dex súbor, novým bajtkódom.
     */
    public static void rewriteToDexFile(byte[] bytes, AppCompatActivity app) {
        try {
            File f = getDexFile(app);
            f.delete();

            File file = new File(getDexDir(app), dexFileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Vráti súbor lístka podľa mena.
     */
    public static File getTicketImage(String filename) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/vlakoveListky/" + filename);
        return myDir;
    }

    /**
     * Skopíruje assets.
     */
    public static void copyAssets(AppCompatActivity context) {
        checkWriteExternalPermission(context);
        FileHelper.context = context;

        //pozriem sa ci v tempe uz nemam
        //nakopirovane subory aby som ich neprevalil
        File tempDir = FileHelper.getTempFolder(context);
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
        if (files == null) {
            return;
        }

        for (String filename : files) {
            if (filesInTemp.contains(filename)) {
                continue;
            }
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(FileHelper.getTempFolder(context), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
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
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * Vymaže lístok z fyzickej pamäte.
     */
    public static boolean deleteTicket(Ticket ticket, AppCompatActivity app) {
        checkWriteExternalPermission(app);
        File f = getTicketImage(ticket.getFilename());
        if (f != null) {
            f.delete();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Vráti priečinok kde sa môže bajtkód nakopírovať, pri loade.
     */
    public static File getOutputDex(AppCompatActivity mActivity) {
        checkWriteExternalPermission(mActivity);
        return mActivity.getDir("dex", Context.MODE_PRIVATE);
    }



}
