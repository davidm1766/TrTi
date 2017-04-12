package sk.listok.zssk.zssklistok;

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


import java.util.ArrayList;

import sk.listok.zssk.zssklistok.classloader.DexDownloader;
import sk.listok.zssk.zssklistok.dataLayer.DatabaseHelper;
import sk.listok.zssk.zssklistok.dataLayer.objects.Person;
import sk.listok.zssk.zssklistok.dataLayer.objects.QueryPerson;
import sk.listok.zssk.zssklistok.dataLayer.objects.QueryTown;
import sk.listok.zssk.zssklistok.dataLayer.objects.Town;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isOnline()){
                    Intent activityChangeIntent = new Intent(MainActivity.this, sk.listok.zssk.zssklistok.activities.FindTrainsActivity.class);
                    MainActivity.this.startActivity(activityChangeIntent);
                } else {
                    Toast.makeText(MainActivity.this,"Skontrolujete svoje internetové pripojenie!", Toast.LENGTH_SHORT).show();
                }


            }

        });

        Button test = (Button) findViewById(R.id.buttonTestNew);
        test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DexDownloader d = new DexDownloader();
            }

        });



        final DatabaseHelper myhelper = new DatabaseHelper(MainActivity.this);
        Button buttondb = (Button) findViewById(R.id.buttonDB);
        buttondb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


           //     QueryTown qt = new QueryTown(myhelper);
           //     Town t = qt.getTownByID(300);
          //      Toast.makeText(MainActivity.this, t.getId() +" - "+ t.getName(), Toast.LENGTH_SHORT).show();

                //qt.getAllTowns();
                // for(Town t : qt.getAllTowns()) {
                //     Toast.makeText(MainActivity.this, t.getId() +" - "+ t.getName(), Toast.LENGTH_SHORT).show();
                // }
                Person pp = new Person("David","Madaras","madaras.david1@gmail.com","1588524","AA123123");
                QueryPerson qp = new QueryPerson(myhelper);
                qp.addPerson(pp);
                ArrayList<Person>  per = qp.getAllPerson();
                for(Person ppp : per){
                    Toast.makeText(MainActivity.this, ppp.getId() +" - "+ ppp.getName(), Toast.LENGTH_SHORT).show();
                }

               // qp.removePerson(per.get(0));



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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




   // public void onClickBtn(View v){



        /*
        WebView browser = (WebView) findViewById(R.id.webview);
        HtmlHelper helper = new HtmlHelper(browser);
        helper.downloadPage();
        */

   // }
}
