package sk.listok.zssk.zssklistok.communication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.util.Stack;

import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.classloader.ClassProvider;
import sk.listok.zssk.zssklistok.helpers.IParserPostData;
import sk.listok.zssk.zssklistok.helpers.ImageHelper;
import sk.listok.zssk.zssklistok.helpers.PostCreator;
import sk.listok.zssk.zssklistok.helpers.PostDataCreatorDynamic;
import sk.listok.zssk.zssklistok.objects.Ticket;

/**
 *  Trieda zaobaluje komunikaciu so serverom a poskytuje všobecné služby.
 */
public class Provider implements INotifyDownloader, INotifyImageDownloaded {

    /**
     * Aktualny dataholder, ktorý sa používa.
     */
    private static DataHolder dataholder;

    /**
     * Na tento interface príde oznámenie o
     * prijatí response od servera.
     */
    private static INotifyDownloader inotify;

    /**
     * Na tento interface pride oznámenie o
     * prijatí stiahnutého obrázku - lístok v .png
     */
    private static INotifyImageDownloaded inotImg;

    /**
     * Inštancia Providera.
     */
    private static Provider instance;

    /**
     * Process dialog, ktorý sa zobrazí pri každej komunikácií
     * so serverom. Zavolá sa na contexte aktivity, ktorá používa
     * dataholder
     */
    private ProgressDialog progressDialog;

    /**
     * Stack do ktorého sa po konci každej aktivity uloži kópia
     * DataHoldera. Pri backbuttone sa popne posledný a tak dostanem
     * predchadzajúci stav.
     */
    private Stack<DataHolder> stackDataholder;

    /**
     * Informácie o lístku, ktoré prenášam medzi aktivitami.
     */
    private Ticket ticket;

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
        ticket = null;
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
            progressDialog = ProgressDialog.show(inotify.getContext(), inotify.getContext().getString(
                    R.string.PROCESSING_DATA), inotify.getContext().getString(R.string.PLEASE_WAIT), true);
        }

        stackDataholder.push(dataholder.clone());
        Provider.dataholder.setPaUrl(Url);
        Provider.dataholder.setPaPOSTdata(POSTdata);
        new Connector(this).execute(Provider.dataholder);
    }


    /**
     *  Vykona POST request na URL s POST datami v parametri.
     */
    public void doRequestDownloadImage(String Url, String POSTdata, INotifyImageDownloaded inot){
        inotImg = inot;
        if(inotify != null && inotify.getContext() != null){
            if(!isOnline(inotify.getContext(),true)){
                //nie som online tak nemozem pokracovať
                return;
            }
            progressDialog = ProgressDialog.show(inotify.getContext(), "Sťahujem lístok", "Prosím čakajte...", true);
        }
        Provider.dataholder.setPaUrl(Url);
        Provider.dataholder.setPaPOSTdata(POSTdata);
        new ImageHelper(this).execute(new Pair<>(Provider.dataholder,Provider.Instance(this).getTicket()));
    }

    /**
     * Vráti cely dataholder
     */
    public static DataHolder getDataholder(){
        return Provider.dataholder;
    }


    /**
     * Oznámenie o vykonení POST requestu, kde pride
     * nastavny Dataholder.
     * @param dh Dataholder, ktorý drži informácie o stránke.
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

    /**
     * Provider neobsahuje Kontext.
     * @return null
     */
    @Override
    public Context getContext()  {
        return null;
    }

    /**
     * Odstráni posledný Dataholder zo stacku.
     */
    public void popDataHolder(){
        Provider.dataholder = this.stackDataholder.pop();
    }

    /**
     * Skontroluje či je používateľ online a podla paramera môže zobraziť správu.
     * @param context Kontext aktivity
     * @param showMessage zobrazí Toast ak je true, inak nezobrazí nič.
     * @return
     */
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

    /**
     * Informacie o lístku, ktoré potrebujem prenášať cez viac aktivít.
     * @return
     */
    public Ticket getTicket(){
        return this.ticket;
    }

    /**
     * Informacie o lístku, ktoré potrebujem prenášať cez viac aktivít.
     */
    public void setTicket(Ticket tic){
        this.ticket = tic;
    }


    /**
     * Vráti parser, prioritne stiahnutý bajtkód, ale ak sa ho nepodarí zaviesť
     * do pamäte použije sa parser v projekte.
     * @param act
     * @return intreface parsera
     */
    public static IParserPostData getIParerInstance(AppCompatActivity act){
        if(ClassProvider.Instance(act).isDexAvaiable()){
            // ak je dostupny parser v dex subore tak pouzijem ten
            //return PostDataCreatorDynamic.Instance(act);
            return new PostCreator();
        } else {
            //inak pouzijem z kodu
            return new PostCreator();
        }
    }

    /**
     * Zmaže celý stack v ktorom su DataHoldre a tiez aktualny.
     */
    public void clearStackAndResetHolder() {
        dataholder = new DataHolder();
        this.stackDataholder.clear();
    }


    @Override
    public void imageDownloaded(Bitmap bitmap) {
        inotImg.imageDownloaded(bitmap);
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}

