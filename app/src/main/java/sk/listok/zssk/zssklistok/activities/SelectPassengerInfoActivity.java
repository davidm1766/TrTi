package sk.listok.zssk.zssklistok.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sk.listok.zssk.zssklistok.dataLayer.DatabaseProvider;
import sk.listok.zssk.zssklistok.dataLayer.objects.Person;
import sk.listok.zssk.zssklistok.dataLayer.objects.QueryPerson;
import sk.listok.zssk.zssklistok.helpers.PostDataCreatorDynamic;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.ParserHelper;
import sk.listok.zssk.zssklistok.communication.DataHolder;


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
        priceInfo.setText("Celková cena s DPH: "+ ParserHelper.ticketPrice(Provider.getDataholder().getPaHtml()));


        Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               Provider.Instance(sk.listok.zssk.zssklistok.activities.SelectPassengerInfoActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/personalData.xhtml",
                        PostDataCreatorDynamic.Instance(SelectPassengerInfoActivity.this).postPassengerInfo(Provider.getDataholder().getPaHtml(), createPerson()));
            }

        });





        this.person = DatabaseProvider.Instance(this).worker().person().getAllPerson();
        final AutoCompleteTextView textView;
        final ArrayAdapter<Person> arrayAdapter = new ArrayAdapter<Person>(this, android.R.layout.simple_dropdown_item_1line,person);

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
    }


    @Override
    public void onBackPressed() {
        Provider.Instance(this).peekDataHolder();
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
        Intent activityChangeIntent = new Intent(sk.listok.zssk.zssklistok.activities.SelectPassengerInfoActivity.this,sk.listok.zssk.zssklistok.activities.SelectFinishPayActivity.class);
        SelectPassengerInfoActivity.this.startActivity(activityChangeIntent);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
