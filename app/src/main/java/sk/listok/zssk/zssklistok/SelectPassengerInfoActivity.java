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

public class SelectPassengerInfoActivity extends AppCompatActivity implements IPharseableHTML {


    private HttpObject ht;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_passenger_info);
        ht = (HttpObject) getIntent().getExtras().getParcelable("HttpObject");


        Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ht.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/personalData.xhtml");
                ht.setPaPOSTdata(parse(ht.getPaHtml()));
        //        ht.setPaHtml(POSTdata(ht));  //treba odkomentit

                Intent activityChangeIntent = new Intent(SelectPassengerInfoActivity.this, SelectFinishPayActivity.class);
                activityChangeIntent.putExtra("HttpObject",ht);
                SelectPassengerInfoActivity.this.startActivity(activityChangeIntent);
            }

        });
    }

    @Override
    public String parse(String html) {
        try {
            Document doc = Jsoup.parse(html);


            Element btn = doc.getElementsContainingOwnText("Pokračovať v platbe").last();
            String selectedLink = btn.attr("onClick");
            int start = selectedLink.indexOf("personalData:");
            int end = selectedLink.indexOf('\'',start);
            int count = end  - start;
            String personalData = selectedLink.substring(start,end);

            Element javaView = doc.getElementById("javax.faces.ViewState");
            String value = javaView.attr("value");

            System.out.println("+++++++++++++++"+ value +"+++++++++++++++++++");

            String finalstr = "ticketParam=ticketParam&"+
                    "personalData=personalData&"+
                    URLEncoder.encode("personalData:payerItemsList:0:field","utf-8")+ "=" + URLEncoder.encode("madaras.david1@gmail.com","utf-8")+"&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:0:field","utf-8")+ "=" + URLEncoder.encode("Dávid","utf-8")+"&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:1:fieldRegId","utf-8")+ "=" + "Madaras&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:2:field","utf-8")+ "=" + "EB793051&"+
                    URLEncoder.encode("personalData:shoppingCartItemList:0:travellerItemsList:3:fieldRegId","utf-8")+ "=" + "1588524&"+
                    URLEncoder.encode("personalData:personalDataAgreement","utf-8")+ "=" + "on&"+
                    "javax.faces.ViewState"+"="+URLEncoder.encode(value,"utf-8")+"&"+
                    URLEncoder.encode(personalData,"utf-8") + "="+URLEncoder.encode(personalData,"utf-8");
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";
    }

    @Override
    public void POSTdata(HttpObject ht) {
      //  new POSTData().execute(this);
    //        return ip.POSTdata(ht);
    }
}
