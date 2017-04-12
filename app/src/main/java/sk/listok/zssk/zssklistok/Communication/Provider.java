package sk.listok.zssk.zssklistok.communication;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


import java.util.Stack;

import sk.listok.zssk.zssklistok.MainActivity;
import sk.listok.zssk.zssklistok.helpers.ImageHelper;

/**
 *  Trieda zaobaluje komunikaciu so serverom.
 */
public class Provider implements INotifyDownloader {

    private static DataHolder dataholder;
    private static INotifyDownloader inotify;
    private static Provider instance;
    private ProgressDialog progressDialog;
    private Stack<DataHolder> stackDataholder;

    public static Provider Instance(INotifyDownloader inotify) {
        if (instance == null) {
            instance = new Provider();
            Provider.dataholder = new DataHolder();
        }
        //potrebujem zmenit inotify, lebo je ine
        if(Provider.inotify != inotify) {
            Provider.inotify = inotify;
        }
        return instance;
    }

    /**
     *  Singleton.
     */
    private Provider(){
        stackDataholder = new Stack<>();
    }

    /**
     *  Vykona POST request na URL s POST datami v parametri.
     */
    public void doRequest(String Url, String POSTdata){
        if(inotify != null && inotify.getContext() != null){
            if(!isOnline(inotify.getContext(),true)){
                //nie som online tak nemozem pokracovať
                return;
            }
            progressDialog = ProgressDialog.show(inotify.getContext(), "Komunikujem so serverom", "Prosím čakajte...", true);
        }

        stackDataholder.push(dataholder.clone());
        Provider.dataholder.setPaUrl(Url);
        Provider.dataholder.setPaPOSTdata(POSTdata);
        new Connector(this).execute(Provider.dataholder);
    }


    /**
     *  Vykona POST request na URL s POST datami v parametri.
     */
    public void doRequestDownloadImage(String Url, String POSTdata){
        Provider.dataholder.setPaUrl(Url);
        Provider.dataholder.setPaPOSTdata(POSTdata);
        new ImageHelper().execute(Provider.dataholder);
    }

    /**
     * Vráti cely dataholder
     * @return
     */
    public static DataHolder getDataholder(){
        return Provider.dataholder;
    }


    /**
     * Oznámenie o vykonení POST requestu, kde pride
     * nastavny Dataholder.
     * @param dh
     */
    @Override
    public void downloaded(DataHolder dh) {
        //sem mi pride ked sa stihne nova html stranka

        inotify.downloaded(dh);
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }

    @Override
    public Context getContext()  {
        return null;
    }

    public void peekDataHolder(){
        Provider.dataholder = this.stackDataholder.pop();
    }

    private boolean isOnline(Context context, boolean showMessage) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        boolean online = netInfo != null && netInfo.isConnectedOrConnecting();
        if(showMessage && !online){
            Toast.makeText(context,"Skontrolujete svoje internetové pripojenie.", Toast.LENGTH_SHORT).show();
        }

        return online;
    }
}

