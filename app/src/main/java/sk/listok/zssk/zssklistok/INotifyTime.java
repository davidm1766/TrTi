package sk.listok.zssk.zssklistok;

/**
 * Interface na oznámenie o zmene hodín a minút, napr. pri výbere z pickera.
 */

public interface INotifyTime {
    void notifyTime(int hour, int minutes);
}
