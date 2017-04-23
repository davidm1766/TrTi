package sk.listok.zssk.zssklistok.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;

import sk.listok.zssk.zssklistok.MainActivity;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.activities.SelectTrainActivity;
import sk.listok.zssk.zssklistok.communication.Provider;

/**
 * Pomocník pri výskyte chyby.
 */
public class ErrorHelper {

    /**
     * Po výskyte chyby po ktorej nie je možné pokračovať
     * sa zobrazí upozornenie, vráti sa na hlavnú aktivitu
     * a vymaže sa stack s dataholdermi.
     */
    public static void onError(final AppCompatActivity context){
        Spanned errorMessage = Html.fromHtml("Vyskytla sa chyba a nie je možné pokračovať."+
                                                "Najčastejšie príčiny vyskytu tejto chyby sú:<br>"+
                                                "<b>1. Na daný vlak nie je možné kúpiť bezplatný lístok<br>"+
                                                "2. Vyplnené údaje nie sú správne</b><br><br>"+
                                                "Ak problém pretrváva kontaktuje vývojárov aplikácie.");
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(errorMessage);
        dlgAlert.setTitle(R.string.ERROR_TITLE);
        dlgAlert.setPositiveButton(R.string.BUTTON_OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                       // ErrorHelper.restartApp(context);
                    }
                });
        dlgAlert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ErrorHelper.restartApp(context);
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


    /**
     * Vrátim sa na hlavnú activity a vymažem
     * stack s dataholdermi.
     * @param context
     */
    private static void restartApp(final AppCompatActivity context){
        Provider.Instance(null).clearStack();
        Intent intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();

    }

}
