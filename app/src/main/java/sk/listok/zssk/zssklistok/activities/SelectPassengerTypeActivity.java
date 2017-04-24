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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Arrays;
import java.util.HashSet;

import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.DataHolder;
import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.AddsHelper;
import sk.listok.zssk.zssklistok.helpers.AlertDialogHelper;
import sk.listok.zssk.zssklistok.helpers.RotationLocker;

public class SelectPassengerTypeActivity extends AppCompatActivity implements INotifyDownloader {

    private Spinner spinner;
    private HashSet<Integer> supportedIndexes = new HashSet<>(Arrays.asList(1, 2, 6, 7, 8));
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_passenger_type);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(AddsHelper.Instance().getAdRequest());
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });


        loadSpinner();

        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int selectedIndex = spinner.getSelectedItemPosition();
                if (!supportedIndexes.contains(selectedIndex)) {
                    Toast.makeText(SelectPassengerTypeActivity.this, R.string.NoSupportedType, Toast.LENGTH_SHORT).show();
                    return;
                }

                String toSend = Provider.getIParerInstance(SelectPassengerTypeActivity.this).postTicketType(Provider.getDataholder().getPaHtml(), selectedIndex);
                if (toSend == null || toSend.equals("")) {
                    AlertDialogHelper.onError(SelectPassengerTypeActivity.this);
                    return;
                }
                RotationLocker.lockRotateScreen(SelectPassengerTypeActivity.this);
                Provider.Instance(SelectPassengerTypeActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/ticketVCD.xhtml",
                        toSend);

            }

        });
    }


    @Override
    public void onBackPressed() {
        Provider.Instance(this).popDataHolder();
        super.onBackPressed();
    }

    /**
     * Načítanie dát do spinnera.
     */
    private void loadSpinner() {
        //poradie sa nemoze menit, index vybratej polozky sa posiela v POST
        String[] arraySpinner = new String[]{
                getString(R.string.CLASSIC),
                getString(R.string.CHILD),
                getString(R.string.STUDENT),
                getString(R.string.JUNIOR),
                getString(R.string.CLASSICRP),
                getString(R.string.SENIORRP),
                getString(R.string.SENIOR62),
                getString(R.string.SENIOR62PLUS),
                getString(R.string.SENIOR70PLUS),
                getString(R.string.TZP),
                getString(R.string.TZPS),
                getString(R.string.DOG)
        };
        this.spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapter);
    }


    @Override
    public void downloaded(DataHolder dh) {
        if (!Provider.getIParerInstance(this).checkNoMoreTickets(dh.getPaHtml()).equals("")) {
            Toast.makeText(this, R.string.NO_MORE_TICKETS, Toast.LENGTH_SHORT).show();
            RotationLocker.lockRotateScreen(SelectPassengerTypeActivity.this);
        } else {
            Intent activityChangeIntent = new Intent(SelectPassengerTypeActivity.this, SelectPassengerInfoActivity.class);
            RotationLocker.lockRotateScreen(SelectPassengerTypeActivity.this);
            SelectPassengerTypeActivity.this.startActivity(activityChangeIntent);
        }

    }

    @Override
    public Context getContext() {
        return this;
    }
}
