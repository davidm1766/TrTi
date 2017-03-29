package sk.listok.zssk.zssklistok;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.util.List;

import sk.listok.zssk.zssklistok.activitiesTmp.FindTrain;
import sk.listok.zssk.zssklistok.dataLayer.Town;
import sk.listok.zssk.zssklistok.sharedData.HtmlHelper;

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
                Intent activityChangeIntent = new Intent(MainActivity.this, SelectTownsActivity.class);
                HttpObject ht = new HttpObject();
                HtmlHelper helper = new HtmlHelper(ht);

                MainActivity.this.startActivity(activityChangeIntent);
            }

        });


        Button buttontest = (Button) findViewById(R.id.buttonTestNew);
        buttontest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(MainActivity.this, FindTrain.class);
                MainActivity.this.startActivity(activityChangeIntent);
            }

        });

        Button buttondb = (Button) findViewById(R.id.buttonDB);
        buttondb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              /*  DatabaseHelper myhelper = new DatabaseHelper(MainActivity.this);
                try{
                    myhelper.createDatabase();
                    myhelper.openDatabase();
                    Toast.makeText(MainActivity.this,"VSETKO OK",Toast.LENGTH_SHORT).show();
                    Cursor c = myhelper.query("TOWN",null,null,null,null,null,null);
                    if(c.moveToFirst()){
                        do{
                            Toast.makeText(MainActivity.this,
                                    "ID="+ c.getString(0) + "\n" +
                                    "NAME="+ c.getString(1) + "\n"
                                    ,Toast.LENGTH_SHORT).show();
                        }while (c.moveToNext());

                    }


                }catch (Exception ex){
                    throw new Error("CHYBA");
                }
*/
                try{


                    OrmLiteSqliteOpenHelper todoOpenDatabaseHelper = OpenHelperManager.getHelper(MainActivity.this,OrmLiteSqliteOpenHelper.class);

                    Dao<Town, Long> todoDao = todoOpenDatabaseHelper.getDao(Town.class);

                   // todoDao.create(new Town(1, "Todo Example 1 Description"));
                   // todoDao.create(new Town(2, "Todo Example 2 Description"));
                   // todoDao.create(new Town(3, "Todo Example 3 Description"));

                    List<Town> todos = todoDao.queryForAll();

                }catch (Exception e){
                    throw new Error(e.toString());
                }
               }

        });

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
