package sk.listok.zssk.zssklistok.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.DataHolder;

public class SelectPassengerTypeActivity extends AppCompatActivity implements INotifyDownloader {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_passenger_type);
        loadSpinner();

        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            Provider.Instance(SelectPassengerTypeActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/ticketVCD.xhtml",
                    Provider.getIParerInstance(SelectPassengerTypeActivity.this).postTicketType(Provider.getDataholder().getPaHtml(), spinner.getSelectedItemPosition()));

            }

        });
    }


    @Override
    public void onBackPressed() {
        Provider.Instance(this).peekDataHolder();
        super.onBackPressed();
    }

    /**
     *  Načítanie dát do spinnera.
     */
    private void loadSpinner(){
        //poradie sa nemoze menit, index vybratej polozky sa posiela v POST
        String[] arraySpinner = new String[] {
                "OBYČAJNÝ",
                "DIEŤA -15",
                "ŽIAK/ŠTUDENT",
                "JUNIOR RP",
                "KLASIK RP",
                "SENIOR RP",
                "DÔCHODCA -62",
                "OBČAN 62+",
                "OBČAN 70+",
                "ŤZP",
                "ŤZP-S",
                "PES"
        };
        this.spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapter);
    }



    @Override
    public void downloaded(DataHolder dh) {
        if(!Provider.getIParerInstance(this).checkNoMoreTickets(dh.getPaHtml()).equals("")){
            Toast.makeText(this,"Vyčerpaný kontigent.",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent activityChangeIntent = new Intent(SelectPassengerTypeActivity.this, SelectPassengerInfoActivity.class);
        SelectPassengerTypeActivity.this.startActivity(activityChangeIntent);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
