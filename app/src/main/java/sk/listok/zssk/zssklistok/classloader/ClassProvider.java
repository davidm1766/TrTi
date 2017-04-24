package sk.listok.zssk.zssklistok.classloader;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Method;

/**
 * Trieda, ktorá slúži na načítanie z disku do pamäte bajtkód parsera.
 */
public class ClassProvider {

    private AppCompatActivity mActivity;
    private static ClassProvider inst;
    private Pair<Class<?>, Object> lastCache;

    public static ClassProvider Instance(AppCompatActivity activity) {
        if (inst == null) {
            inst = new ClassProvider(activity);
        }
        return inst;
    }

    private ClassProvider(AppCompatActivity activity) {
        this.mActivity = activity;
        lastCache = null;
    }

    private ClassProvider() {
        //singleton
    }


    /**
     * Pokiaľ ešte nie je načítaný bajtkód v pamäti
     * načíta ho, ak je už zavedený do pamäte, tak
     * nič nenačítava, len ho vráti zo svojej cache.
     */
    private void loadParser() {

        //cache nech nenatahujem stale...
        if (lastCache != null && lastCache.first != null && lastCache.second != null) {
            return;
        }

        LoadParser lp = new LoadParser(mActivity);
        Class<?> cl = lp.loadClassCore();

        try {
            Object lastInstance = cl.newInstance();
            this.lastCache = new Pair(cl, lastInstance);
        } catch (Exception ex) {
            this.lastCache = null;
        }

    }


    /**
     * Zavolanie metody na načítanom bajtkóde parsera.
     *
     * @param methodName  Názov metódy
     * @param paramTypes  Typy parametrov
     * @param paramValues Hodnoty parametrov
     * @return
     */
    public Object getMethodResult(String methodName, Class[] paramTypes, Object... paramValues) {
        try {
            loadParser(); //aktualizujem cache ak su prazdne...
            Method postFindTrains = lastCache.first.getDeclaredMethod(methodName, paramTypes);
            return postFindTrains.invoke(lastCache.second, paramValues);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Funkcia skontroluje či sa podarilo načítať bajtkod
     * parsera do pamäte a či je tam zavedený.
     *
     * @return true = bajtkod parsera je v pamäti
     */
    public boolean isDexAvaiable() {
        loadParser();
        if (lastCache == null) {
            return false;
        }
        return true;
    }
}
