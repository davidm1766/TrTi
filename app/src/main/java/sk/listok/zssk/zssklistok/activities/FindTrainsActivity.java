package sk.listok.zssk.zssklistok.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.INotifyDate;
import sk.listok.zssk.zssklistok.INotifyTime;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.helpers.PostDataCreatorDynamic;
import sk.listok.zssk.zssklistok.helpers.TrainForParser;
import sk.listok.zssk.zssklistok.communication.DataHolder;
import sk.listok.zssk.zssklistok.objects.Ticket;


public class FindTrainsActivity extends AppCompatActivity implements INotifyTime,INotifyDate, INotifyDownloader {


    private boolean isFromTown;
    private TextView twFromTown;
    private TextView twToTown;
    private FrameLayout containerFragment;
    private FrameLayout containerFragmentThis;
    private TextView twTime;
    private TextView twDate;
    private TimePickerFragment newTimeFragment;
    private DatePickerFragment newDateFragment;
    TrainSearchFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_towns);
        initComponents();
        initHandlers();
    }

    @Override
    public void onBackPressed() {
        if (containerFragment != null && containerFragment.getVisibility() == View.VISIBLE){
            containerFragment.setVisibility(View.INVISIBLE);
            containerFragmentThis.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }

    /**
     * Inicializacia všetkých odchytávačov eventov.
     */
    private void initHandlers(){
        //tlacidlo pokracovat dalej
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TrainForParser tr = getTrain();
                Provider.Instance(FindTrainsActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/connection/portal.xhtml",
                        PostDataCreatorDynamic.Instance(FindTrainsActivity.this).postFindTrains(tr));
                //timestamp kedy kupujem listok
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
                String format = s.format(new Date());
                Provider.Instance(FindTrainsActivity.this).setTicket(new Ticket(tr.getTownFrom(),tr.getTownTo(),format));
            }
        });

        //vyber casu
        LinearLayout etTime = (LinearLayout) findViewById(R.id.areaTime);
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTimeFragment.show( getFragmentManager(), "timePicker");
            }
        });

        //vyber datumu
        final LinearLayout etDate = (LinearLayout) findViewById(R.id.areaDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDateFragment.show( getFragmentManager(), "datePicker");
            }
        });

        //odchytenie vybrateho mesta
        final ListView lv = (ListView) findViewById(R.id.listViewTrain);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lv.getItemAtPosition(position);
                lv.getSelectedItem();

                //po vybrati mesta prehodim fragmenty
                containerFragment.setVisibility(View.INVISIBLE);
                containerFragmentThis.setVisibility(View.VISIBLE);

                //vybrate mesto priradim podla toho odklial bol fragment volany
                if(isFromTown){
                    twFromTown.setText(lv.getItemAtPosition(position).toString());
                }else {
                    twToTown.setText(lv.getItemAtPosition(position).toString());
                }
            }

        });

        //vyber mesta z
        twFromTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.clearFilter();
                isFromTown = true;
                displayStationsFragment();
            }
        });

        //vyber mesta Do
        twToTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.clearFilter();
                isFromTown = false;
                displayStationsFragment();
            }
        });
    }


    /**
     * Zobrazenie fragmentu so stanicami.
     */
    private void displayStationsFragment(){
        containerFragment.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.clearFilter();
        containerFragmentThis.setVisibility(View.INVISIBLE);
        fragmentTransaction.commit();
    }


    /**
     * Vráti vyplnené údaje v aktivite v podobe objektu.
     * @return
     */
    public TrainForParser getTrain(){
        String townFrom = twFromTown.getText().toString();
        String townTo = twToTown.getText().toString();
        String time = twTime.getText().toString();
        String date = twDate.getText().toString();
        return new TrainForParser(townFrom,townTo,time, date);
    }


    /**
     * Inicializácia komponentov
     */
    private void initComponents(){
        this.twFromTown = (TextView) findViewById(R.id.textViewFromTown);
        this.twToTown =  (TextView) findViewById(R.id.textViewToTown);
        this.containerFragment = (FrameLayout) findViewById(R.id.container_fragment);
        containerFragment.setVisibility(View.INVISIBLE);
        this.containerFragmentThis = (FrameLayout) findViewById(R.id.container_fragment1);
        this.twTime = (TextView) findViewById(R.id.textViewTime);
        this.twDate = (TextView) findViewById(R.id.textViewDate);
        this.newTimeFragment = new TimePickerFragment();
        newTimeFragment.setINotifiable(FindTrainsActivity.this);
        this.newDateFragment = new DatePickerFragment();
        newDateFragment.setINotifiable(FindTrainsActivity.this);


        fragment = (TrainSearchFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

    }


    @Override
    public void notifyTime(int hour, int minutes) {
        if (minutes < 10){
            twTime.setText(hour+":0"+ minutes);
        } else {
            twTime.setText(hour+":"+ minutes);
        }
    }

    @Override
    public void notifyDate(int day, int month, int year) {
        twDate.setText(day+"."+month+"."+ year);
    }

    @Override
    public void downloaded(DataHolder dh) {

        Intent activityChangeIntent = new Intent(FindTrainsActivity.this, SelectTrainActivity.class);
        FindTrainsActivity.this.startActivity(activityChangeIntent);

    }

    @Override
    public Context getContext() {
        return this;
    }
}
