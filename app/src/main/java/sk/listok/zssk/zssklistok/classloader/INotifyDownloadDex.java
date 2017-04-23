package sk.listok.zssk.zssklistok.classloader;

/**
 * Slúži na notifikácie pri sťahovaní .dex súboru z API
 */
public interface INotifyDownloadDex {
    /**
     * Po skončení sťahovania z API vráti výsledok v dexDownloadInfo
     * @param dexDownloadInfo výsledok sťahovania .dex
     */
    void DownloadedDex(DexDownloadInfo dexDownloadInfo);
}
