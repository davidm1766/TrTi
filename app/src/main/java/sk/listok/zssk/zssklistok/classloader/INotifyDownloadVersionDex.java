package sk.listok.zssk.zssklistok.classloader;

/**
 * Slúži na notifikácie pri sťahovaní čísla verzie z API
 */

public interface INotifyDownloadVersionDex {
    /**
     * Po skončení sťahovania čísla verzie z API
     * vráti číslo verzie stringu.
     * @param content
     */
    void Downloaded(String content);
}
