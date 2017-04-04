package sk.listok.zssk.zssklistok.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import sk.listok.zssk.zssklistok.objects.PersonInfo;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.ParserHelper;
import sk.listok.zssk.zssklistok.helpers.PostDataCreator;
import sk.listok.zssk.zssklistok.communication.DataHolder;


public class SelectPassengerInfoActivity extends AppCompatActivity implements INotifyDownloader {

    private TextView finalInfo;
    private TextView priceInfo;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_passenger_info);


        finalInfo = (TextView) findViewById(R.id.textViewFinalDetails);
        priceInfo = (TextView) findViewById(R.id.textViewPrice);

        // nastavim detail z listka
        finalInfo.setText(ParserHelper.ticketDetails(Provider.getDataholder().getPaHtml()));
        // nastavim cenu
        priceInfo.setText("Celková cena s DPH: "+ ParserHelper.ticketPrice(Provider.getDataholder().getPaHtml()));


        Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Provider.Instance(sk.listok.zssk.zssklistok.activities.SelectPassengerInfoActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/personalData.xhtml",
                        PostDataCreator.postPassengerInfo(Provider.getDataholder().getPaHtml(), createPerson()));
                progressDialog = ProgressDialog.show(sk.listok.zssk.zssklistok.activities.SelectPassengerInfoActivity.this, "Spracúvavam údaje", "Prosím čakajte...", true);
            }

        });
    }


    public PersonInfo createPerson(){
        //person = new PersonInfo("madaras.david1@gmail.com","Dávid","Madaras","EB793051","1588524");
        String email = ((EditText)findViewById(R.id.editTextEmail)).getText().toString();
        String name = ((EditText)findViewById(R.id.editTextName)).getText().toString();
        String surname =((EditText)findViewById(R.id.editTextSurname)).getText().toString() ;
        String regNum = ((EditText)findViewById(R.id.editTextRegNumber)).getText().toString();

        return new PersonInfo(email,name,surname,regNum);

    }


    @Override
    public void downloaded(DataHolder dh) {
        //DataHolder.setInst(dh); //LEN KVOLI TESTOVANIU
        progressDialog.dismiss();
        Intent activityChangeIntent = new Intent(sk.listok.zssk.zssklistok.activities.SelectPassengerInfoActivity.this,sk.listok.zssk.zssklistok.activities.SelectFinishPayActivity.class);
        SelectPassengerInfoActivity.this.startActivity(activityChangeIntent);
    }
}
