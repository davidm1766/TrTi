package sk.listok.zssk.zssklistok.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import sk.listok.zssk.zssklistok.MainActivity;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.helpers.FileHelper;
import sk.listok.zssk.zssklistok.objects.Ticket;

public class MyTicketsActivity extends AppCompatActivity {

    private ListView lv;
    private Ticket selectedItemOnLong;
    private View ticketToRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);
        selectedItemOnLong = null;
        lv = (ListView) findViewById(R.id.listViewTicket);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ticket t = (Ticket) parent.getAdapter().getItem(position);
                File ticketImg = FileHelper.getTicketImage(t.getFilename());
                Intent i = new Intent();
                i.setAction(android.content.Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(ticketImg), "image/png");
                startActivity(i);
            }
        });


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemOnLong = (Ticket) parent.getAdapter().getItem(position);
                ticketToRemove = view;
                showAlert();
                return true;
            }
        });


        if (lv.getCount() == 0) {
            TextView tv = (TextView) findViewById(R.id.textViewTicketNotFound);
            tv.setVisibility(View.VISIBLE);
        } else {
            TextView tv = (TextView) findViewById(R.id.textViewTicketNotFound);
            tv.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * Zobrazenie upozornenie o odstránení lístka a jeho vymazanie.
     */
    private void showAlert() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(R.string.SURE_DELETE);
        dlgAlert.setTitle(R.string.WARRNING);
        dlgAlert.setPositiveButton(R.string.YES,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (FileHelper.deleteTicket(selectedItemOnLong)) {
                            Toast.makeText(MyTicketsActivity.this, R.string.TICKET_DELETE_SUCCESS, Toast.LENGTH_SHORT).show();
                            TicketsFramagment fragment = (TicketsFramagment) getSupportFragmentManager().findFragmentById(R.id.fragmentTicket);
                            fragment.adapter.remove(selectedItemOnLong);
                            fragment.adapter.notifyDataSetChanged();
                            lv.refreshDrawableState();
                        } else {
                            Toast.makeText(MyTicketsActivity.this, R.string.DELETE_TICKET_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dlgAlert.setNegativeButton(R.string.NO,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //používateľ nechce zmazať lístok
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyTicketsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MyTicketsActivity.this.startActivity(intent);
        //super.onBackPressed();
    }
}
