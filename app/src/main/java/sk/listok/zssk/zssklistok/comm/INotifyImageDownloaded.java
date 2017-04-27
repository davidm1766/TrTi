package sk.listok.zssk.zssklistok.comm;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

/**
 * Slúži na oznámenie o dokončení sťahovania obrázku.
 * Konkrétne lístok v podobe .png
 */
public interface INotifyImageDownloaded {
    /**
     * Oznámenie o stiahnutí lístka v podobne .png
     *
     * @param bitmap
     */
    void imageDownloaded(Bitmap bitmap);

    /**
     * Vráti aktivitu, je to kvoli zápisu do pamäte.
     * @return
     */
    AppCompatActivity getCompatActivity();
}
