package sk.listok.zssk.zssklistok.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import sk.listok.zssk.zssklistok.helpers.INotifyDate;

/**
 * Fragment na výber dátumu.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private INotifyDate ind;

    /**
     * Nastavenie interfaceu, ktorý bude prijímať oznámenia.
     *
     * @param ind na tento interface sa odošle oznámenie,
     *            keď sa vyberie dátum
     */
    public void setINotifiable(INotifyDate ind) {
        this.ind = ind;
        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        ind.notifyDate(day, month + 1, year);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * Po nastavení dátumu sa pomocou INotifyDate interface,
     * dá vedieť, že dátum bol zmenený.
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (ind != null) {
            ind.notifyDate(day, month + 1, year);
        }
    }

}
