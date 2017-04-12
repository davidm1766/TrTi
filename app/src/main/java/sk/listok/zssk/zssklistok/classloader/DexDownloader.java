package sk.listok.zssk.zssklistok.classloader;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


import dalvik.system.DexClassLoader;

/**
 * Created by Nexi on 10.04.2017.
 */

public class DexDownloader {


    public DexDownloader(){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL website = new URL("https://train.file.core.windows.net/Fil");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream("Final.dex");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }).start();
    }

    private void test(){


    }

}
