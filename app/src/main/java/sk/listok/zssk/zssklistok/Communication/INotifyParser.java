package sk.listok.zssk.zssklistok.communication;

import android.content.Context;
import java.util.ArrayList;
import sk.listok.zssk.zssklistok.objects.JourneyData;

/**
 * Notifikácia o dokončení parsovania spojov.
 */

public interface INotifyParser {
    /**
     * Vráti rozparsované spoje v arrayliste.
     * @param data
     */
    void parsered(ArrayList<JourneyData> data);

    /**
     * Kontext aktivity, ktorá používa parser.
     * @return
     */
    Context getContext();
}
