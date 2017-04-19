package sk.listok.zssk.zssklistok.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import sk.listok.zssk.zssklistok.MainActivity;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.FileHelper;
import sk.listok.zssk.zssklistok.helpers.ImageHelper;
import sk.listok.zssk.zssklistok.objects.Ticket;

public class MyTicketsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        final ListView lv = (ListView) findViewById(R.id.listViewTicket);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ticket t = (Ticket)parent.getAdapter().getItem(position);

                File ticketImg = FileHelper.getTicketImage(t.getFilename());
                Intent i = new Intent();
                i.setAction(android.content.Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(ticketImg), "image/png");
                startActivity(i);
            }
        });

        if (lv.getCount() == 0){
            TextView tv = (TextView) findViewById(R.id.textViewTicketNotFound);
            tv.setVisibility(View.VISIBLE);
        }else {
            TextView tv = (TextView) findViewById(R.id.textViewTicketNotFound);
            tv.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        Intent activityChangeIntent = new Intent(MyTicketsActivity.this,MainActivity.class);
        MyTicketsActivity.this.startActivity(activityChangeIntent);
        //super.onBackPressed();
    }
}
