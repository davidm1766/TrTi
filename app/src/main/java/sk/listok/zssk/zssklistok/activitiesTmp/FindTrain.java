package sk.listok.zssk.zssklistok.activitiesTmp;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import sk.listok.zssk.zssklistok.INotifyDate;
import sk.listok.zssk.zssklistok.INotifyTime;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.findTrainPageFirst.DatePickerFragment;
import sk.listok.zssk.zssklistok.findTrainPageFirst.TimePickerFragment;
import sk.listok.zssk.zssklistok.sharedData.DataHolder;

public class FindTrain extends AppCompatActivity implements INotifyDate,INotifyTime {



    private boolean isFromTown;
    private DataHolder dh = DataHolder.getInst();
    private TextView twFromTown;
    private TextView twToTown;
    private FrameLayout containerFragment;
    private FrameLayout containerFragmentThis;
    private TextView twTime;
    private TextView twDate;
    private TimePickerFragment newTimeFragment;
    private DatePickerFragment newDateFragment;
    StationsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_train);
        initComponents();
        initHandlers();

    }

    private void initHandlers() {
        //vyber mesta Do
        twToTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.clearFilter();
                isFromTown = false;
                containerFragment.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                fragment.clearFilter();
                containerFragmentThis.setVisibility(View.INVISIBLE);

                fragmentTransaction.commit();


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


/*
        StationsFragment newFragment = new StationsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.containerFragmentStations, newFragment);
        //transaction.addToBackStack(null);

        transaction.commit();
        */
    }


    private void initComponents(){
        this.twFromTown = (TextView) findViewById(R.id.textViewFromTown);
        this.twToTown =  (TextView) findViewById(R.id.textViewToTown);
        //this.containerFragment = (FrameLayout) findViewById(R.id.container_fragment);
        //containerFragment.setVisibility(View.INVISIBLE);
        //this.containerFragmentThis = (FrameLayout) findViewById(R.id.container_fragment1);
        this.twTime = (TextView) findViewById(R.id.textViewTime);
        this.twDate = (TextView) findViewById(R.id.textViewDate);
        this.newTimeFragment = new TimePickerFragment();
        newTimeFragment.setINotifiable(FindTrain.this);
        this.newDateFragment = new DatePickerFragment();
        newDateFragment.setINotifiable(FindTrain.this);
        fragment = (StationsFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

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
}
