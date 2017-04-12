package sk.listok.zssk.zssklistok.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.DataHolder;
import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.PostDataCreatorDynamic;


public class SelectFinishPayActivity extends AppCompatActivity implements INotifyDownloader {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_finish_pay);


        Button button = (Button) findViewById(R.id.buttonFinish);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Provider.Instance(SelectFinishPayActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/payment.xhtml",
                        PostDataCreatorDynamic.Instance(SelectFinishPayActivity.this).postFinishPayment(Provider.getDataholder().getPaHtml()));
            }

        });
    }

    @Override
    public void onBackPressed() {
        Provider.Instance(this).peekDataHolder();
    }

    @Override
    public void downloaded(DataHolder dh) {
        //stiahnutie a ulozenie obrazka
        Provider.Instance(SelectFinishPayActivity.this).doRequestDownloadImage("https://ikvc.slovakrail.sk/inet-sales-web/pages/payment/travelDocument.xhtml",
                PostDataCreatorDynamic.Instance(this).postDownloadTicket(Provider.getDataholder().getPaHtml()));

    }

    @Override
    public Context getContext() {
        return this;
    }
}
