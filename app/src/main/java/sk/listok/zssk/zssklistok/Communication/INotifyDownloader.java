package sk.listok.zssk.zssklistok.communication;

import android.content.Context;

/**
 * Slúži na oznámenie o dokončení requestu na zssk server.
 */
public interface INotifyDownloader {

    /**
     * Oznámenie o response od zssk servera.
     * @param dh obsahuje aj HTML, ktoré vrátil server.
     */
    void downloaded(DataHolder dh);

    /**
     * Kontext aktivity, ktorá používa sťahovanie.
     * @return
     */
    Context getContext();
}
