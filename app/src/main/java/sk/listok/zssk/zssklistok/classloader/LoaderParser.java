package sk.listok.zssk.zssklistok.classloader;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.URLEncoder;


import org.jsoup.nodes.Element;

import dalvik.system.DexClassLoader;


/**
 * Created by Nexi on 06.04.2017.
 */

public class LoaderParser {

    AppCompatActivity mActivity;
    Element s;
    Document d;
    URLEncoder ss;
    Jsoup js;
    Elements els;

    public LoaderParser(AppCompatActivity act){
        this.mActivity = act;
    }

    public void runTest(){

        try{
            SelectImplementation();

        }catch (Exception ex){
            System.out.println(ex.toString());
        }

    }

    private void SelectImplementation() {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/vlakoveListky");
        myDir.mkdirs();

        //LenTakTrieda efi = null;

        String defaultLibPath =  myDir + "/Final.dex";
        File dexOutputDir = mActivity.getDir("dex", Context.MODE_PRIVATE);
        DexClassLoader dcl = new DexClassLoader(defaultLibPath,
                dexOutputDir.getAbsolutePath(),
                null,
                ClassLoader.getSystemClassLoader());

        try {

            Class<?> clazz = dcl.loadClass("sk.listok.zssk.zssklistok.helpers.PostCreator");
          //  efi = (LenTakTrieda) clazz.newInstance();
            Class[] cArg = new Class[2];
            cArg[0] = String.class;
            cArg[1] = String.class;

            Method m = clazz.getDeclaredMethod("postSelectTrain",cArg);
            final Object myInstance  = clazz.newInstance();
            this.d = new Document("");
            ClassLoader xb = (ClassLoader) m.invoke(myInstance,"","");

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return efi;

    }


}


