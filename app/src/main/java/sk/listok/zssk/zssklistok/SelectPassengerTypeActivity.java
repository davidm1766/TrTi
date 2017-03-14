package sk.listok.zssk.zssklistok;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URLEncoder;

public class SelectPassengerTypeActivity extends AppCompatActivity implements IPostable {

    private DataHolder dh = DataHolder.getInst();
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_passenger_type);
        loadSpinner();

        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dh.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/ticketVCD.xhtml");
                dh.setPaPOSTdata(createPOSTData(dh.getPaHtml()));
                POSTDataSync ret = new POSTDataSync();
                dh.setPaHtml(ret.POSTDataSyncFunc(dh));

                Intent activityChangeIntent = new Intent(SelectPassengerTypeActivity.this, SelectPassengerInfoActivity.class);
               // activityChangeIntent.putExtra("HttpObject",dh);
                SelectPassengerTypeActivity.this.startActivity(activityChangeIntent);

            }

        });
    }

    private void loadSpinner(){
        //poradie sa nemoze menit, index vybratej polozky sa posiela v POST
        String[] arraySpinner = new String[] {
                "OBYČAJNÝ",
                "DIEŤA -15",
                "ŽIAK/ŠTUDENT",
                "JUNIOR RP",
                "KLASIK RP",
                "SENIOR RP",
                "DÔCHODCA -62",
                "OBČAN 62+",
                "OBČAN 70+",
                "ŤZP",
                "ŤZP-S",
                "PES"
        };
        this.spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        this.spinner.setAdapter(adapter);
    }


    @Override
    public String createPOSTData(String html) {
        try {
            Document doc = Jsoup.parse(html);


            Element ticketPassenger = doc.getElementsByAttributeValueContaining("name","ticketParam:passenger:").first();
            String tiPass = ticketPassenger.attr("name");


            Element btn = doc.getElementsContainingOwnText("Rýchly nákup").first();
            String selectedLink = btn.attr("onClick");
            int start = selectedLink.indexOf("ticketParam:");
            int end = selectedLink.indexOf('\\',start);
            int count = end  - start;
            String tiParam = selectedLink.substring(start,end);

            Element javaView = doc.getElementById("javax.faces.ViewState");
            String value = javaView.attr("value");

            System.out.println("+++++++++++++++"+ value +"+++++++++++++++++++");

            String finalstr = "ticketParam=ticketParam&"+
                    URLEncoder.encode(tiPass,"utf-8") + "=" + ""+ this.spinner.getSelectedItemPosition()+"&" +        //cislo podla indexu vybratej polozky v comboboxe
                    URLEncoder.encode("ticketParam:passenger:0:contingentCheck","utf-8") + "=on&"+   // BEZPLATNE IBA PRI STUDENTOVI
                    "javax.faces.ViewState"+"="+URLEncoder.encode(value,"utf-8")+"&"+
                    URLEncoder.encode(tiParam,"utf-8") + "="+URLEncoder.encode(tiParam,"utf-8");
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";
    }

}
