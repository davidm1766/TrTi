package sk.listok.zssk.zssklistok.classloader;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Method;

/**
 * Created by Nexi on 10.04.2017.
 */

public class ClassProvider {

    private AppCompatActivity mActivity;
    private static ClassProvider inst;
    private Pair<Class<?>,Object> lastCache;

    public static ClassProvider Instance(AppCompatActivity activity){
        if (inst == null){
            inst = new ClassProvider(activity);
        }
        return inst;
    }

    private ClassProvider(AppCompatActivity activity){
        this.mActivity = activity;
        lastCache = null;
    }

    private ClassProvider(){
        //singleton
    }
    private void refreshLoadedParser(){

        //cache nech nenatahujem stale...
        if(lastCache != null && lastCache.first != null && lastCache.second != null) {
            return;
        }

        LoadParser lp = new LoadParser(mActivity);
        Class<?> cl = lp.loadClassCore();

        try {
            Object lastInstance = cl.newInstance();
            this.lastCache = new Pair(cl,lastInstance);
        }catch (Exception ex){
            this.lastCache = null;
        }

    }


    public Object getMethodResult(String methodName, Class[] paramTypes, Object... paramValues){
        try {
            refreshLoadedParser(); //aktualizujem cache ak su prazdne...
            Method postFindTrains = lastCache.first.getDeclaredMethod(methodName, paramTypes);
            return postFindTrains.invoke(lastCache.second, paramValues);
        }catch (Exception ex ){
            ex.printStackTrace();
            return null;
        }
    }


    public boolean isDexAvaiable(){
        refreshLoadedParser();
        if(lastCache == null){
            return false;
        }
        return true;
    }
}
