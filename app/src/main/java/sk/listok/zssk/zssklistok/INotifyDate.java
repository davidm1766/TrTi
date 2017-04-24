package sk.listok.zssk.zssklistok;

/**
 * Slúži na oznámenie o vybratí dátumu, napr. z pickera.
 */

public interface INotifyDate {
    void notifyDate(int day, int month, int year);
}
