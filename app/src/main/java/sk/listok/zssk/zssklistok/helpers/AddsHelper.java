package sk.listok.zssk.zssklistok.helpers;

import com.google.android.gms.ads.AdRequest;

/**
 * Created by Nexi on 20.04.2017.
 */

public class AddsHelper {

    private AdRequest adRequest;


    private static AddsHelper inst;
    public static AddsHelper Instance(){
        if (inst == null){
            inst = new AddsHelper();
        }
        return inst;
    }


    private AddsHelper(){
        this.adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E8AAF6D1FAACD33793ACBCFC167B405F")
                .build();
    }

    public AdRequest getAdRequest(){
        return this.adRequest;
    }


}
