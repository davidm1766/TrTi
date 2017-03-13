package sk.listok.zssk.zssklistok;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URLEncoder;

public class SelectPassengerTypeActivity extends AppCompatActivity implements IPharseableHTML {


    private HttpObject ht;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_passenger_type);
        ht = (HttpObject) getIntent().getExtras().getParcelable("HttpObject");

        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ht.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/ticketVCD.xhtml");
                ht.setPaPOSTdata(parse(ht.getPaHtml()));
              //  ht.setPaHtml(POSTdata(ht));

                Intent activityChangeIntent = new Intent(SelectPassengerTypeActivity.this, SelectPassengerInfoActivity.class);
                activityChangeIntent.putExtra("HttpObject",ht);
                SelectPassengerTypeActivity.this.startActivity(activityChangeIntent);

            }

        });
    }

    @Override
    public String parse(String html) {
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
                    URLEncoder.encode(tiPass,"utf-8") + "=" + "2&" +
                    URLEncoder.encode("ticketParam:passenger:0:contingentCheck","utf-8") + "=on&"+   // BEZPLATNE IBA PRI STUDENTOVI
                    "javax.faces.ViewState"+"="+URLEncoder.encode(value,"utf-8")+"&"+
                    URLEncoder.encode(tiParam,"utf-8") + "="+URLEncoder.encode(tiParam,"utf-8");
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";
    }

    @Override
    public void POSTdata(HttpObject ht) {
    //    IPOSTData ip = new POSTData();
     //   return ip.POSTdata(ht);
    }
}
