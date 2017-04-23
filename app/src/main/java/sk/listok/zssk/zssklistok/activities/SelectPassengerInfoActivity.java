package sk.listok.zssk.zssklistok.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sk.listok.zssk.zssklistok.helpers.RotationLocker;
import sk.listok.zssk.zssklistok.datalayer.DatabaseProvider;
import sk.listok.zssk.zssklistok.datalayer.objects.Person;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.ErrorHelper;
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
    private String ticketDetailsText;
    private String ticketPricetText;


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
        ticketDetailsText = Provider.getIParerInstance(this).ticketDetails(Provider.getDataholder().getPaHtml());
        finalInfo.setText(ticketDetailsText);
        // nastavim cenu
        ticketPricetText = getString(R.string.PRICE_WITH_TAX)+
                Provider.getIParerInstance(this).ticketPrice(Provider.getDataholder().getPaHtml());
        priceInfo.setText(ticketPricetText);


        Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Person per = createPerson();
                ArrayList<Person> pe = DatabaseProvider.Instance(SelectPassengerInfoActivity.this).
                        worker().person().getPersonByName(per.getName(),per.getSurname());
                if(pe == null ||pe.size()==0){
                    DatabaseProvider.Instance(SelectPassengerInfoActivity.this).worker().person().removePerson(per);
                    DatabaseProvider.Instance(SelectPassengerInfoActivity.this).worker().person().addPerson(per);
                }
                Provider pro = Provider.Instance(SelectPassengerInfoActivity.this);
                String toSend = pro.getIParerInstance(SelectPassengerInfoActivity.this).postPassengerInfo(
                        Provider.getDataholder().getPaHtml(), per.getEmail(),per.getName(),per.getSurname(),per.getRegNumber());
                if(toSend == null || toSend.equals("")){
                    ErrorHelper.onError(SelectPassengerInfoActivity.this);
                    return;
                }
                RotationLocker.lockRotateScreen(SelectPassengerInfoActivity.this);
                pro.doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/personalData.xhtml",
                        toSend);
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
                    showDropdown(textView);
                    isDispleyed = true;
                } else {
                    dismissDropdown(textView);
                    isDispleyed = false;
                }

            }
        });

        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDropdown(textView);
                    isDispleyed = true;
                } else {
                    dismissDropdown(textView);
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


    private void showDropdown(final AutoCompleteTextView textView){
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.showDropDown();
            }
        });
    }
    private void dismissDropdown(final AutoCompleteTextView textView){
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.dismissDropDown();
            }
        });
    }



    @Override
    public void onBackPressed() {
        Provider.Instance(this).popDataHolder();
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
        RotationLocker.unlockRotateScreen(SelectPassengerInfoActivity.this);
        if(!result.equals("")){
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent activityChangeIntent = new Intent(sk.listok.zssk.zssklistok.activities.SelectPassengerInfoActivity.this,sk.listok.zssk.zssklistok.activities.SelectFinishPayActivity.class);
        activityChangeIntent.putExtra("price",ticketPricetText);
        activityChangeIntent.putExtra("detail",ticketDetailsText);
        SelectPassengerInfoActivity.this.startActivity(activityChangeIntent);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
