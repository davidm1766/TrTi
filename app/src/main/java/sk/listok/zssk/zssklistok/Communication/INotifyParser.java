package sk.listok.zssk.zssklistok.communication;

import android.content.Context;

import java.util.ArrayList;

import sk.listok.zssk.zssklistok.objects.JourneyData;

/**
 * Created by Nexi on 28.03.2017.
 */

public interface INotifyParser {
    public void parsered(ArrayList<JourneyData> data);
    public Context getContext();
}
