package sk.listok.zssk.zssklistok.classloader;

/**
 * Created by Nexi on 22.04.2017.
 */

public class DexUpdater implements INotifyDownloadVersionDex {


    public DexUpdater(){

    }

    public void chceckUpdates(){
        new DexCheckVersion(this).execute();
    }


    @Override
    public void Downloaded(String content) {
        return;
    }
}
