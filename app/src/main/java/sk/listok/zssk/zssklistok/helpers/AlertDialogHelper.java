package sk.listok.zssk.zssklistok.helpers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import sk.listok.zssk.zssklistok.MainActivity;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.communication.Provider;

/**
 * Pomocník pri zobrazovní hlášok.
 */
public class AlertDialogHelper {

    /**
     * Po výskyte chyby po ktorej nie je možné pokračovať
     * sa zobrazí upozornenie, vráti sa na hlavnú aktivitu
     * a vymaže sa stack s dataholdermi.
     */
    public static void onError(final AppCompatActivity context){

        TextView message = new TextView(context);
        message.setPadding(30,30,30,30);

        Spanned errorMessage = Html.fromHtml("Vyskytla sa chyba a nie je možné pokračovať. "+
                "Najčastejšie príčiny vyskytu tejto chyby sú:<br>"+
                "<b>1. Na daný vlak nie je možné kúpiť bezplatný lístok<br>"+
                "2. Vyplnené údaje nie sú správne</b><br><br>"+
                "Ak problém pretrváva, prosím kontaktuje vývojárov aplikácie na <a href=\"mailto:aplikaciavlaky@gmail.com\" target=\"_top\">aplikaciavlaky@gmail.com</a>.");

        final SpannableString s =
                new SpannableString(errorMessage);
        Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES);
        message.setText(s);

        message.setMovementMethod(LinkMovementMethod.getInstance());


        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);

        dlgAlert.setView(message);
        dlgAlert.setTitle(R.string.ERROR_TITLE);
        dlgAlert.setPositiveButton(R.string.BUTTON_OK,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //tu to nemusim riesit lebo sa to riesi v ondismiss
                         //AlertDialogHelper.restartApp(context);
                    }
                });
        dlgAlert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                AlertDialogHelper.restartApp(context);
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
        Provider.Instance(null).clearStackAndResetHolder();
        Intent intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();

    }



    /**
     * Pokiaľ chce zákazník zrušiť objednávku tak
     * sa zobrazí upozornenie, vráti sa na hlavnú aktivitu
     * a vymaže sa stack s dataholdermi.
     */
    public static void onCancelPurchase(final AppCompatActivity context){
        String warningMessage = "Naozaj si prajete zrušiť nákup?";
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(warningMessage);
        dlgAlert.setTitle(R.string.WARRNING);
        dlgAlert.setPositiveButton(R.string.YES,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialogHelper.restartApp(context);
                    }
                });
        dlgAlert.setNegativeButton(R.string.NO,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Nechce ukončit nákup
                    }
                });

        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }



}
