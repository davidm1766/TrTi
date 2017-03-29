package sk.listok.zssk.zssklistok.findTrainPageFirst;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import sk.listok.zssk.zssklistok.INotifyTime;

/**
 * Created by Nexi on 05.03.2017.
 */

public class TimePickerFragment extends DialogFragment  implements TimePickerDialog.OnTimeSetListener {


    private INotifyTime it;

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        if(it != null){
            it.notifyTime(hourOfDay,minute);
        }
    }

    public void setINotifiable(INotifyTime it){

        this.it = it;

        java.util.Calendar c = java.util.Calendar.getInstance();

        int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = c.get(java.util.Calendar.MINUTE);

        it.notifyTime(hour,minute);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        java.util.Calendar c = java.util.Calendar.getInstance();

        int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = c.get(java.util.Calendar.MINUTE);


        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }


}
