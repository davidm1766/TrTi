package sk.listok.zssk.zssklistok.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;

import sk.listok.zssk.zssklistok.MainActivity;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.DataHolder;
import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.ImageHelper;
import sk.listok.zssk.zssklistok.helpers.PostDataCreator;


public class SelectFinishPayActivity extends AppCompatActivity implements INotifyDownloader {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_finish_pay);


        Button button = (Button) findViewById(R.id.buttonFinish);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Provider.Instance(SelectFinishPayActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/payment.xhtml",
                        PostDataCreator.postFinishPayment(Provider.getDataholder().getPaHtml()));
            }

        });
    }

    @Override
    public void downloaded(DataHolder dh) {
        //stiahnutie a ulozenie obrazka
        Provider.Instance(SelectFinishPayActivity.this).doRequestDownloadImage("https://ikvc.slovakrail.sk/inet-sales-web/pages/payment/travelDocument.xhtml",
                PostDataCreator.postDownloadTicket(Provider.getDataholder().getPaHtml()));

    }
}
