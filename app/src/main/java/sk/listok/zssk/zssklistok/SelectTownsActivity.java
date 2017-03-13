package sk.listok.zssk.zssklistok;

//import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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

public class SelectTownsActivity extends AppCompatActivity implements IPharseableHTML, INotifyTime,INotifyDate {



    private DataHolder dh = DataHolder.getInst();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_towns);

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
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setINotifiable(SelectTownsActivity.this);
                newFragment.show( getFragmentManager(), "timePicker");
            }
        });

        //vyber datumu
        LinearLayout etDate = (LinearLayout) findViewById(R.id.areaDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setINotifiable(SelectTownsActivity.this);
                newFragment.show( getFragmentManager(), "datePicker");

            }
        });





        FrameLayout fl = (FrameLayout) findViewById(R.id.container_fragment);
        fl.setVisibility(View.INVISIBLE);



        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                FrameLayout fl = (FrameLayout) findViewById(R.id.container_fragment);
                fl.setVisibility(View.INVISIBLE);

                FrameLayout f2 = (FrameLayout) findViewById(R.id.container_fragment1);
                f2.setVisibility(View.VISIBLE);




            }

        });


        //vyber mesta
        LinearLayout etFromTown = (LinearLayout) findViewById(R.id.LLFromTown);
        etFromTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FrameLayout f2 = (FrameLayout) findViewById(R.id.container_fragment);
                f2.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                TrainSearchFragment fragment = (TrainSearchFragment)fragmentManager.findFragmentById(R.id.fragment);// new TrainSearchFragment();

                FrameLayout fl = (FrameLayout) findViewById(R.id.container_fragment1);
                fl.setVisibility(View.INVISIBLE);

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
        dh.setPaPOSTdata(parse(dh.getPaHtml()));
        POSTDataSync ret = new POSTDataSync();
        return ret.POSTDataSyncFunc(ht);
    }













    @Override
    public String parse(String html) {
        String townFrom = "Tepli%C4%8Dka%20nad%20Horn%C3%A1dom";//"%C5%BDilina";
        String townTo = "Zvolen";
        return "lang=sk&portal=&from="+townFrom+"&to="+townTo+"&via=&date=4.3.2017&time=18%3A41&departure=true";
    }


    @Override
    public void POSTdata(HttpObject ht) {
        //POSTData.getInstance().execute(this);
    }

    public String test (DataHolder ht){
        POSTDataSync ps = new POSTDataSync();
        return ps.POSTDataSyncFunc(ht);
    }


    @Override
    public void notifyTime(int hour, int minutes) {

        TextView twTime = (TextView) findViewById(R.id.textViewTime);
        twTime.setText(hour+":"+ minutes);

    }

    @Override
    public void notifyDate(int day, int month, int year) {
        TextView twTime = (TextView) findViewById(R.id.textViewDate);
        twTime.setText(day+"."+month+"."+ year);
    }
}
