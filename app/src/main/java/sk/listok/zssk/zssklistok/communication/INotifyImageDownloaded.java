package sk.listok.zssk.zssklistok.communication;

import android.graphics.Bitmap;

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
}
