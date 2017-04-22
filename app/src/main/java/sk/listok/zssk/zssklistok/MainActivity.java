package sk.listok.zssk.zssklistok;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import sk.listok.zssk.zssklistok.activities.Reklama;
import sk.listok.zssk.zssklistok.classloader.DexDownloader;
import sk.listok.zssk.zssklistok.dataLayer.DatabaseHelper;
import sk.listok.zssk.zssklistok.dataLayer.DatabaseProvider;
import sk.listok.zssk.zssklistok.dataLayer.objects.Person;
import sk.listok.zssk.zssklistok.dataLayer.objects.QueryPerson;
import sk.listok.zssk.zssklistok.dataLayer.objects.QueryTown;
import sk.listok.zssk.zssklistok.dataLayer.objects.Town;
import sk.listok.zssk.zssklistok.helpers.AddsHelper;
import sk.listok.zssk.zssklistok.helpers.FileHelper;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //load reklam
        AddsHelper.Instance().getAdRequest();

        FileHelper.copyAssets(this); //skopirujem si subory...
        Button button = (Button) findViewById(R.id.buttonStartShopping);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isOnline()){
                    //najprv si rozbalim parser
                    //DexDownloader d = new DexDownloader();
//                    Intent a = new Intent(MainActivity.this, Reklama.class);
//                    MainActivity.this.startActivity(a);

                    ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, getString(R.string.LOADING_STATIONS), getString(R.string.PLEASE_WAIT), true);

                    Intent activityChangeIntent = new Intent(MainActivity.this, sk.listok.zssk.zssklistok.activities.FindTrainsActivity.class);
                    MainActivity.this.startActivity(activityChangeIntent);

                    progressDialog.dismiss();
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


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


}
