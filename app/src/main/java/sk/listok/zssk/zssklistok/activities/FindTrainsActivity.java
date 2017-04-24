package sk.listok.zssk.zssklistok.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import sk.listok.zssk.zssklistok.INotifyDate;
import sk.listok.zssk.zssklistok.INotifyTime;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.DataHolder;
import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.AlertDialogHelper;
import sk.listok.zssk.zssklistok.helpers.RotationLocker;
import sk.listok.zssk.zssklistok.helpers.TrainForParser;
import sk.listok.zssk.zssklistok.objects.Ticket;


public class FindTrainsActivity extends AppCompatActivity implements INotifyTime, INotifyDate, INotifyDownloader {


    /**
     * Zdeilané preferencie na uloženie posledného mesta
     * z ktorého a do ktorého používateľ dal vyhľadávať.
     */
    private String trainPreferences = "trainPref";
    SharedPreferences sharedpreferences;

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
    private int whichFragmentOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_towns);
        initComponents();
        initHandlers();
        sharedpreferences = getSharedPreferences(trainPreferences, Context.MODE_PRIVATE);

        //nacitam zo shared preferencies posledne vyhladane spoje
        twFromTown.setText(sharedpreferences.getString("townFrom", twFromTown.getText().toString()));
        twToTown.setText(sharedpreferences.getString("townTo", twToTown.getText().toString()));

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int fragOpen = savedInstanceState.getInt("fragmentOpen");
            switch (fragOpen) {
                case 0:
                    //nic nebolo otvorene som na acitvite
                    break;
                case 1:
                    //bolo otvorene z mesta
                    showFragmentFromTown();
                    break;
                case 2:
                    //bolo otvorene do mesta
                    showFragmentToTown();
                    break;
            }
            whichFragmentOpen = fragOpen;
        }
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("fragmentOpen", whichFragmentOpen);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (containerFragment != null && containerFragment.getVisibility() == View.VISIBLE) {
            containerFragment.setVisibility(View.INVISIBLE);
            containerFragmentThis.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("townFrom", twFromTown.getText().toString());
        editor.putString("townTo", twToTown.getText().toString());
        editor.commit();

        super.onPause();
    }


    /**
     * Inicializacia všetkých odchytávačov eventov.
     */
    private void initHandlers() {
        //tlacidlo pokracovat dalej
        Button button = (Button) findViewById(R.id.nextButtonFind);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TrainForParser tr = getTrain();
                String toSend = Provider.getIParerInstance(FindTrainsActivity.this).postFindTrains(tr.getTownFrom(), tr.getTownTo(), tr.getTime(), tr.getDate());
                if (toSend == null || toSend.equals("")) {
                    AlertDialogHelper.onError(FindTrainsActivity.this);
                    return;
                }
                RotationLocker.lockRotateScreen(FindTrainsActivity.this);
                Provider.Instance(FindTrainsActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/connection/portal.xhtml",
                        toSend);
                //timestamp kedy kupujem listok
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
                String format = s.format(new Date());
                Provider.Instance(FindTrainsActivity.this).setTicket(new Ticket(tr.getTownFrom(), tr.getTownTo(), format));
            }
        });

        //vyber casu
        LinearLayout etTime = (LinearLayout) findViewById(R.id.areaTime);
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTimeFragment.show(getFragmentManager(), "timePicker");
            }
        });

        //vyber datumu
        final LinearLayout etDate = (LinearLayout) findViewById(R.id.areaDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        //odchytenie vybrateho mesta
        final ListView lv = (ListView) findViewById(R.id.listViewTrain);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lv.getItemAtPosition(position);
                lv.getSelectedItem();

                //po vybrati mesta prehodim fragmenty
                containerFragment.setVisibility(View.INVISIBLE);
                containerFragmentThis.setVisibility(View.VISIBLE);

                //vybrate mesto priradim podla toho odklial bol fragment volany
                if (isFromTown) {
                    twFromTown.setText(lv.getItemAtPosition(position).toString());
                } else {
                    twToTown.setText(lv.getItemAtPosition(position).toString());
                }
                /* vratil som sa z fragmentu na vyber mesta
                 * a preto nastavim ze nie som uz na ziadnom frag.
                 */
                whichFragmentOpen = 0;
            }

        });

        //vyber mesta z
        twFromTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentFromTown();
            }
        });

        //vyber mesta Do
        twToTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentToTown();
            }
        });
    }

    /**
     * Zobrazenie fragmentu s mestami, pričom výsledok sa
     * uloží do "z mesta"
     */
    private void showFragmentFromTown() {
        fragment.clearFilter();
        isFromTown = true;
        whichFragmentOpen = 1;
        displayStationsFragment();
    }

    /**
     * Zobrazenie fragmentu s mestami, pričom výsledok sa
     * uloží do "do mesta".
     */
    private void showFragmentToTown() {
        fragment.clearFilter();
        isFromTown = false;
        whichFragmentOpen = 2;
        displayStationsFragment();
    }


    /**
     * Zobrazenie fragmentu so stanicami.
     */
    private void displayStationsFragment() {
        containerFragment.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.clearFilter();
        containerFragmentThis.setVisibility(View.INVISIBLE);
        fragmentTransaction.commit();

    }


    /**
     * Vráti vyplnené údaje v aktivite v podobe objektu.
     *
     * @return
     */
    public TrainForParser getTrain() {
        String townFrom = twFromTown.getText().toString();
        String townTo = twToTown.getText().toString();
        String time = twTime.getText().toString();
        String date = twDate.getText().toString();
        return new TrainForParser(townFrom, townTo, time, date);
    }


    /**
     * Inicializácia komponentov
     */
    private void initComponents() {
        this.twFromTown = (TextView) findViewById(R.id.textViewFromTown);
        this.twToTown = (TextView) findViewById(R.id.textViewToTown);
        this.containerFragment = (FrameLayout) findViewById(R.id.container_fragment);
        containerFragment.setVisibility(View.INVISIBLE);
        this.containerFragmentThis = (FrameLayout) findViewById(R.id.container_fragment_another);
        this.twTime = (TextView) findViewById(R.id.textViewTime);
        this.twDate = (TextView) findViewById(R.id.textViewDate);
        this.newTimeFragment = new TimePickerFragment();
        newTimeFragment.setINotifiable(FindTrainsActivity.this);
        this.newDateFragment = new DatePickerFragment();
        newDateFragment.setINotifiable(FindTrainsActivity.this);
        fragment = (TrainSearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
    }


    @Override
    public void notifyTime(int hour, int minutes) {
        if (minutes < 10) {
            twTime.setText(hour + ":0" + minutes);
        } else {
            twTime.setText(hour + ":" + minutes);
        }
    }

    @Override
    public void notifyDate(int day, int month, int year) {
        twDate.setText(day + "." + month + "." + year);
    }

    @Override
    public void downloaded(DataHolder dh) {
        RotationLocker.unlockRotateScreen(FindTrainsActivity.this);
        Intent activityChangeIntent = new Intent(FindTrainsActivity.this, SelectTrainActivity.class);
        FindTrainsActivity.this.startActivity(activityChangeIntent);

    }

    @Override
    public Context getContext() {
        return this;
    }
}
