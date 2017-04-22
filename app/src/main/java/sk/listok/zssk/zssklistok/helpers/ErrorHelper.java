package sk.listok.zssk.zssklistok.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import sk.listok.zssk.zssklistok.MainActivity;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.activities.SelectTrainActivity;
import sk.listok.zssk.zssklistok.communication.Provider;

/**
 * Created by Nexi on 22.04.2017.
 */

public class ErrorHelper {


    public static void onError(final AppCompatActivity context){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(R.string.ERROR_MESSAGE);
        dlgAlert.setTitle(R.string.ERROR_TITLE);
        dlgAlert.setPositiveButton(R.string.BUTTON_OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                        Provider.Instance(null).clearStack();
                        Intent intent = new Intent(context,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        context.finish();

                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


}
