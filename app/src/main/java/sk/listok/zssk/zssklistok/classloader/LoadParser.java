package sk.listok.zssk.zssklistok.classloader;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import dalvik.system.DexClassLoader;
import sk.listok.zssk.zssklistok.helpers.FileHelper;


/**
 * Samotné načítanie parsera z dátového úložiska.
 */
public class LoadParser {

    AppCompatActivity mActivity;

    public LoadParser(AppCompatActivity act){
        this.mActivity = act;
    }


    /**
     * Jadro načítania bajtkódu do pamäte.
     * @return vráti načítanu triedu parsera
     */
    public Class<?> loadClassCore() {

        File myDir = FileHelper.getDexFile(); // FileHelper.getTicketFolder();
        File dexOutputDir = FileHelper.getOutputDex(mActivity);
        String defaultLibPath =  myDir + "";
        DexClassLoader dcl = new DexClassLoader(defaultLibPath,
                dexOutputDir.getAbsolutePath(),
                null,
                ClassLoader.getSystemClassLoader());

        Class<?> clazz = null;
        try {
            clazz = dcl.loadClass("sk.listok.zssk.zssklistok.helpers.PostCreator");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return clazz;
    }


    /**
     * Pre rýchly test či je metóda v parseri OK.
     * @param clazz
     * @param myInstance
     */
    private void testAllMethods(Class<?> clazz, Object myInstance){
        try {
            //test druhej fcie
            //Method postSelectTrain = clazz.getDeclaredMethod("postSelectTrain",new Class[]{String.class,String.class});
            //postSelectTrain.invoke(myInstance,"param1","param2");

            //test tretej fcie
           // Method postFindTrains = clazz.getDeclaredMethod("postFindTrains",new Class[]{String.class,String.class,String.class,String.class});
            // postFindTrains.invoke(myInstance,"MestoZ","MestoDo","10:10","19.10.2016");

           // Method postTicketType.txt = clazz.getDeclaredMethod("postTicketType.txt",new Class[]{String.class,int.class});
            //postTicketType.txt.invoke(myInstance,"param1",1);

            //test stvrtej fcie
            //Method postPassengerInfo = clazz.getDeclaredMethod("postPassengerInfo",new Class[]{String.class, String.class,String.class,String.class,String.class});
            //postPassengerInfo.invoke(myInstance,"htmlcode","email","meno","priezvisko","regnumber");

            //test piatej fcie
            //Method postFinishPayment = clazz.getDeclaredMethod("postFinishPayment",new Class[]{String.class});
            //postFinishPayment.invoke(myInstance,"htmlcode");

            //test siestej fcie
            Method postDownloadTicket = clazz.getDeclaredMethod("postDownloadTicket",new Class[]{String.class});
            postDownloadTicket.invoke(myInstance,"htmlcode");


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}

