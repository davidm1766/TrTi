package sk.listok.zssk.zssklistok.helpers;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import sk.listok.zssk.zssklistok.communication.INotifyParser;
import sk.listok.zssk.zssklistok.objects.JourneyData;
import sk.listok.zssk.zssklistok.objects.TrainData;

/**
 * Parser na vytvorenie objektov ciest vlakov z html.
 * Je to naročnejšia operácia preto beži na pozadí.
 */
public class ParserFoundTrains extends AsyncTask<String, Void, ArrayList<JourneyData>> {


    private INotifyParser inotify;
    private ProgressDialog progressDialog;

    /**
     * Na interface vrátim oznámenie keď sa
     * parsovanie dokočí
     */
    public ParserFoundTrains(INotifyParser in) {
        if (in == null) {
            throw new IllegalArgumentException("INotifyParser is null!");
        }
        inotify = in;
    }


    @Override
    protected ArrayList<JourneyData> doInBackground(String... params) {
        ArrayList<JourneyData> fullList = new ArrayList<>();
        try {
        String html = params[0];
        Document doc = Jsoup.parse(html);
        Elements allLists = doc.select(".tmp-train-list");



            for (Element e : allLists) {

                // vytiahnem si vsetky riadky bude ich parny pocet
                Elements tbody = e.select("table").first().children().select("tbody:not([class])");
                ArrayList<TrainData> listTrains = new ArrayList<>(); // vsetky vlaky na ktore sa prestupuje na jednej ceste
                TrainData ret = new TrainData();
                int i = 1;
                // prestupy v ramci jednej cesty
                for (Element e1 : tbody) {

                    if (i % 2 == 1) {
                        ret = new TrainData();
                        ret.setNameTrain(e1.select("tr").get(0).select(".connection-segment-name").html());

                        Elements tds = e1.select("tr").get(1).select("td");
                        ret.setFromTown(tds.get(1).html());
                        ret.setDepartueDate(tds.get(2).html());
                        ret.setDepartueTime(tds.get(3).select("strong").html());
                        ret.setJourneyTime(tds.get(4).select("strong").html());
                    }

                    if (i % 2 == 0) {
                        Elements tds = e1.select("td:not([class])");
                        ret.setToTown(tds.get(0).html());
                        ret.setArrivalDate(tds.get(1).html());
                        ret.setArrivalTime(tds.get(2).select("strong").html());
                        listTrains.add(ret);
                    }

                    i++;
                }
                JourneyData jr = new JourneyData();
                jr.setTrainData(listTrains);
                fullList.add(jr);

            }

            int j = 0;
            //zistim si celkovy cas cesty
            Elements times = doc.select("td > .tmp-bold");
            for (Element t : times) {
                if (t.html().contains("min")) {
                    fullList.get(j).setJourneyTime(t.html());
                    j++;
                }
            }

            //este zistim idcka z butonov na kupenie
            Elements btns = doc.select(".tmp-buy-btns");

            for (int i = 0; i < btns.size(); i++) {
                for (Element el : btns.get(i).select("a")) {
                    if (el.html().equals("Lístok")) {
                        String idstr = el.attr("onClick");
                        int first = idstr.indexOf("inetConnection");
                        String fin = idstr.substring(first - 11, idstr.indexOf('\'', first)); // 11 - searachConnection:inetConnection

                        fullList.get(i).setIdJourney(fin);
                    }
                }
                //String idstr = btns.get(i).select("a").last().attr("onClick");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return fullList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (inotify.getContext() != null) {
            progressDialog = ProgressDialog.show(inotify.getContext(), "Spracovávam údaje", "Prosím čakajte...", true);
        }
    }

    @Override
    protected void onPostExecute(ArrayList<JourneyData> journeyDatas) {
        inotify.parsered(journeyDatas);
        if (inotify.getContext() != null && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            progressDialog = null;
        }
    }


}
