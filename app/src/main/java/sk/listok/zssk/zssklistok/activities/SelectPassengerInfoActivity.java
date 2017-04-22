package sk.listok.zssk.zssklistok.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

import sk.listok.zssk.zssklistok.dataLayer.DatabaseProvider;
import sk.listok.zssk.zssklistok.dataLayer.objects.Person;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.AddsHelper;
import sk.listok.zssk.zssklistok.helpers.ParserHelper;
import sk.listok.zssk.zssklistok.communication.DataHolder;
import sk.listok.zssk.zssklistok.helpers.VerifyHelper;


public class SelectPassengerInfoActivity extends AppCompatActivity implements INotifyDownloader {

    private TextView finalInfo;
    private TextView priceInfo;
    private ArrayList<Person> person;
    private EditText editTextEmail;
    private EditText editTextName;
    private AutoCompleteTextView editTextSurname;
    private EditText editTextRegNumber;
    private boolean isDispleyed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_passenger_info);


        editTextEmail = ((EditText)findViewById(R.id.editTextEmail));
        editTextName = ((EditText) findViewById(R.id.editTextName));
        editTextSurname = ((AutoCompleteTextView)findViewById(R.id.autoCompleteTextViewSurname));
        editTextRegNumber = ((EditText)findViewById(R.id.editTextRegNumber));

        finalInfo = (TextView) findViewById(R.id.textViewFinalDetails);
        priceInfo = (TextView) findViewById(R.id.textViewPrice);

        isDispleyed = false;
        // nastavim detail z listka
        finalInfo.setText(ParserHelper.ticketDetails(Provider.getDataholder().getPaHtml()));
        // nastavim cenu
        priceInfo.setText(getString(R.string.PRICE_WITH_TAX)+ ParserHelper.ticketPrice(Provider.getDataholder().getPaHtml()));


        Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Person per = createPerson();
                ArrayList<Person> pe = DatabaseProvider.Instance(SelectPassengerInfoActivity.this).
                        worker().person().getPersonByName(per.getName(),per.getSurname());
                if(pe== null ||pe.size()==0){
                    DatabaseProvider.Instance(SelectPassengerInfoActivity.this).worker().person().removePerson(per);
                    DatabaseProvider.Instance(SelectPassengerInfoActivity.this).worker().person().addPerson(per);
                }
                Provider pro = Provider.Instance(SelectPassengerInfoActivity.this);

                pro.doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/personalData.xhtml",
                        pro.getIParerInstance(SelectPassengerInfoActivity.this).postPassengerInfo(
                                Provider.getDataholder().getPaHtml(), per.getEmail(),per.getName(),per.getSurname(),per.getRegNumber()));
            }

        });





        this.person = DatabaseProvider.Instance(this).worker().person().getAllPerson();
        final AutoCompleteTextView textView;
        final ArrayAdapter<Person> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,person);



        textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewSurname);
        textView.setAdapter(arrayAdapter);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                if (!isDispleyed){
                    textView.showDropDown();
                    isDispleyed = true;
                } else {
                    textView.dismissDropDown();
                    isDispleyed = false;
                }

            }
        });

        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    textView.showDropDown();
                    isDispleyed = true;
                } else {
                    textView.dismissDropDown();
                    isDispleyed = false;
                }
            }
        });


//        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(SelectPassengerInfoActivity.this);
//        dlgAlert.setMessage("This is an alert with no consequence");
//        dlgAlert.setTitle("App Title");
//        dlgAlert.setPositiveButton("OK", null);
//        dlgAlert.setCancelable(true);


        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person per = (Person)parent.getAdapter().getItem(position);
                SelectPassengerInfoActivity.this.editTextRegNumber.setText(per.getRegNumber());
                SelectPassengerInfoActivity.this.editTextName.setText(per.getName());
                SelectPassengerInfoActivity.this.editTextSurname.setText(per.getSurname());
                SelectPassengerInfoActivity.this.editTextEmail.setText(per.getEmail());
            }

        });


//
//        dlgAlert.setPositiveButton("Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //dismiss the dialog
//                        //ano chcem zmazat  a mazem
//                    }
//                });
    }


    @Override
    public void onBackPressed() {
        Provider.Instance(this).peekDataHolder();
        super.onBackPressed();
    }

    public Person createPerson(){
        String email = editTextEmail.getText().toString();
        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString() ;
        String regNum = editTextRegNumber.getText().toString();

        return new Person(name,surname,email,regNum);

    }


    @Override
    public void downloaded(DataHolder dh) {
        String result = VerifyHelper.checkUserInfo(dh.getPaHtml());
        if(!result.equals("")){
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent activityChangeIntent = new Intent(sk.listok.zssk.zssklistok.activities.SelectPassengerInfoActivity.this,sk.listok.zssk.zssklistok.activities.SelectFinishPayActivity.class);
        SelectPassengerInfoActivity.this.startActivity(activityChangeIntent);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
