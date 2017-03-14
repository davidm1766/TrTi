package sk.listok.zssk.zssklistok;

//import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;


public class SelectTownsActivity extends AppCompatActivity implements IPostable, INotifyTime,INotifyDate {


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
    TrainSearchFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_towns);

        initComponents();


        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final ProgressDialog progressDialog= ProgressDialog.show(SelectTownsActivity.this, "Vyhľadávanie vlakov",
                        "Prosím čakajte...", true);
                subscription = getPOSTData(dh)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent activityChangeIntent = new Intent(SelectTownsActivity.this, SelectTrainActivity.class);
                                        progressDialog.dismiss();
                                        SelectTownsActivity.this.startActivity(activityChangeIntent);
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                System.out.println(e.toString()+"CHYBA rx");
                            }

                            @Override
                            public void onNext(String s) {
                                dh.setPaHtml(s);
                            }
                        });
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
                //here you can use the position to determine what checkbox to check
                //this assumes that you have an array of your checkboxes as well. called checkbox
                lv.getItemAtPosition(position);
                lv.getSelectedItem();

                containerFragment.setVisibility(View.INVISIBLE);
                containerFragmentThis.setVisibility(View.VISIBLE);

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
                containerFragment.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //TrainSearchFragment fragment = (TrainSearchFragment)fragmentManager.findFragmentById(R.id.fragment);// new TrainSearchFragment();

                containerFragmentThis.setVisibility(View.INVISIBLE);
                fragmentTransaction.commit();


            }
        });



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
    }




    private Subscription subscription;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    public Observable<String> getPOSTData(final DataHolder ht){
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                try{
                    return Observable.just(getHtmlPage(ht));
                }catch (Exception e){
                    System.out.println(e.toString());
                    return null;
                }
            }
        });
    }


    @Nullable
    public String getHtmlPage(DataHolder ht){
        dh.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/connection/portal.xhtml");
        dh.setPaPOSTdata(createPOSTData(dh.getPaHtml()));
        POSTDataSync ret = new POSTDataSync();
        return ret.POSTDataSyncFunc(ht);
    }



    @Override
    public String createPOSTData(String html) {
        String townFrom = Uri.encode(twFromTown.getText().toString());//"Tepli%C4%8Dka%20nad%20Horn%C3%A1dom";//"%C5%BDilina";
        String townTo = Uri.encode(twToTown.getText().toString()); //"Zvolen";
        String time = Uri.encode(twTime.getText().toString()); //18%3A41
        String date = Uri.encode(twDate.getText().toString()); //4.3.2017
        return "lang=sk&portal=&from="+townFrom+"&to="+townTo+"&via=&date="+date+"&time="+time+"&departure=true";
    }


    private void initComponents(){
        this.twFromTown = (TextView) findViewById(R.id.textViewFromTown);
        this.twToTown =  (TextView) findViewById(R.id.textViewToTown);
        this.containerFragment = (FrameLayout) findViewById(R.id.container_fragment);
        containerFragment.setVisibility(View.INVISIBLE);
        this.containerFragmentThis = (FrameLayout) findViewById(R.id.container_fragment1);
        this.twTime = (TextView) findViewById(R.id.textViewTime);
        this.twDate = (TextView) findViewById(R.id.textViewDate);
        this.newTimeFragment = new TimePickerFragment();
        newTimeFragment.setINotifiable(SelectTownsActivity.this);
        this.newDateFragment = new DatePickerFragment();
        newDateFragment.setINotifiable(SelectTownsActivity.this);
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
}
