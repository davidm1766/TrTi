package sk.listok.zssk.zssklistok;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import sk.listok.zssk.zssklistok.classloader.DexCheckVersion;
import sk.listok.zssk.zssklistok.classloader.DexDownloadInfo;
import sk.listok.zssk.zssklistok.classloader.DexDownloader;
import sk.listok.zssk.zssklistok.classloader.INotifyDownloadDex;
import sk.listok.zssk.zssklistok.classloader.INotifyDownloadVersionDex;
import sk.listok.zssk.zssklistok.classloader.eStatus;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.AddsHelper;
import sk.listok.zssk.zssklistok.helpers.FileHelper;


public class MainActivity extends AppCompatActivity implements INotifyDownloadVersionDex, INotifyDownloadDex {

    private SharedPreferences sharedpreferences;
    private ProgressDialog progressDialog;
    private String newVersionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences("lastVersion", Context.MODE_PRIVATE);

        Provider.Instance(null).clearStackAndResetHolder();
        //load reklam
        AddsHelper.Instance().getAdRequest();
        FileHelper.copyAssets(this); //skopirujem si subory a DB ak nie je...

        Button button = (Button) findViewById(R.id.buttonStartShopping);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (isOnline()) {
                    progressDialog = ProgressDialog.show(MainActivity.this, getString(R.string.LOADING_STATIONS), getString(R.string.PLEASE_WAIT), true);
                    new DexCheckVersion(MainActivity.this).execute();
                } else {
                    Toast.makeText(MainActivity.this, R.string.CHECK_CONNECTION, Toast.LENGTH_SHORT).show();
                }

            }

        });

        Button tick = (Button) findViewById(R.id.buttonMyTicket);
        tick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(MainActivity.this, sk.listok.zssk.zssklistok.activities.MyTicketsActivity.class);
                MainActivity.this.startActivity(activityChangeIntent);

            }

        });


    }


    /**
     * Funkcia na zistenie či je používateľ online.
     *
     * @return true ak je online, false ak nie je pripojený na internet
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Spustím prvú aktivitu na nákup lístka.
     */
    private void startBuyingActivity() {
        Intent activityChangeIntent = new Intent(MainActivity.this, sk.listok.zssk.zssklistok.activities.FindTrainsActivity.class);
        progressDialog.dismiss();
        MainActivity.this.startActivity(activityChangeIntent);
    }


    @Override
    public void Downloaded(String content) {
        String lastDownloadedVersion = sharedpreferences.getString("version", null);
        if (lastDownloadedVersion == null || !lastDownloadedVersion.equals(content)) {
            //Nesedi verzia
            newVersionNumber = content;
            new DexDownloader(this).execute();
        }
        startBuyingActivity();
    }


    @Override
    public void DownloadedDex(DexDownloadInfo info) {
        if (info.getStatus() == eStatus.OK) {
            progressDialog = ProgressDialog.show(MainActivity.this, getString(R.string.UPDATING), getString(R.string.PLEASE_WAIT), true);
            FileHelper.rewriteToDexFile(info.getDexBytes(),this);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("version", newVersionNumber);
            editor.commit();
            progressDialog.dismiss();
        }
        startBuyingActivity();

    }

}
