package sk.listok.zssk.zssklistok.communication;

import android.content.Context;

/**
 * Created by Nexi on 24.03.2017.
 */

public interface INotifyDownloader {
    void downloaded(DataHolder dh);
    Context getContext();
    //public Context getContext();
}
