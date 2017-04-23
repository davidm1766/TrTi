package sk.listok.zssk.zssklistok.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import sk.listok.zssk.zssklistok.MainActivity;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.classloader.RotationLocker;
import sk.listok.zssk.zssklistok.communication.DataHolder;
import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.INotifyImageDownloaded;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.ErrorHelper;


public class SelectFinishPayActivity extends AppCompatActivity implements INotifyDownloader,INotifyImageDownloaded {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_finish_pay);



        Button button = (Button) findViewById(R.id.buttonFinish);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String toSend = Provider.getIParerInstance(SelectFinishPayActivity.this).postFinishPayment(Provider.getDataholder().getPaHtml());
                if(toSend == null || toSend.equals("")){
                    ErrorHelper.onError(SelectFinishPayActivity.this);
                    return;
                }
                RotationLocker.lockRotateScreen(SelectFinishPayActivity.this);
                Provider.Instance(SelectFinishPayActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/payment.xhtml",
                        toSend);
            }

        });
    }

    @Override
    public void onBackPressed() {
        Provider.Instance(this).peekDataHolder();
        super.onBackPressed();
    }

    @Override
    public void downloaded(DataHolder dh) {
        //stiahnutie a ulozenie obrazka
        Provider.Instance(SelectFinishPayActivity.this).doRequestDownloadImage("https://ikvc.slovakrail.sk/inet-sales-web/pages/payment/travelDocument.xhtml",
                Provider.getIParerInstance(this).postDownloadTicket(Provider.getDataholder().getPaHtml()),this);

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void imageDownloaded(Bitmap bitmap) {
        Intent activityChangeIntent = new Intent(SelectFinishPayActivity.this,MyTicketsActivity.class);
        RotationLocker.unlockRotateScreen(this);
        SelectFinishPayActivity.this.startActivity(activityChangeIntent);
    }
}
