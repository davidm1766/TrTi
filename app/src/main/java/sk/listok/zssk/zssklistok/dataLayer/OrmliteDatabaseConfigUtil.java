package sk.listok.zssk.zssklistok.dataLayer;

/**
 * Created by Nexi on 20.03.2017.
 */

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * OrmliteDatabaseConfigUtil is a separate program from the actual android app,
 * that is used to generate the database structure configuration before runtime.
 * It uses the models provided via a list of class objects,
 * and also the annotations (e.g. @DatabaseField) on the models to generate the configuration accordingly.
 */
public class OrmliteDatabaseConfigUtil extends OrmLiteConfigUtil {

    /**
     * classes represents the models to use for generating the ormlite_config.txt file
     */
    private static final Class<?>[] classes = new Class[] {Town.class};

    public static void main(String[] args) throws IOException, SQLException {
//        writeConfigFile("ormlite_config.txt");

        String currDirectory = "user.dir";

       String configPath = "/app/src/main/res/raw/ormlite_config.txt";


        String projectRoot = System.getProperty(currDirectory);


        String fullConfigPath = projectRoot + configPath;

        File configFile = new File(fullConfigPath);


        if(configFile.exists()) {
            configFile.delete();
            configFile = new File(fullConfigPath);
        }


        writeConfigFile(configFile, classes);

    }
}
