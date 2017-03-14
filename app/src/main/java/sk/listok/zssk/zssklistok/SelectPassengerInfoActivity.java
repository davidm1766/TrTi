package sk.listok.zssk.zssklistok;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;

public class SelectPassengerInfoActivity extends AppCompatActivity implements IPostable {

    private DataHolder dh = DataHolder.getInst();
    private PersonInfo person;
    private TextView finalInfo;
    private TextView priceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_passenger_info);
        person = new PersonInfo("madaras.david1@gmail.com","Dávid","Madaras","EB793051","1588524");

        finalInfo = (TextView) findViewById(R.id.textViewFinalDetails);
        priceInfo = (TextView) findViewById(R.id.textViewPrice);

        // nastavim detail z listka
        finalInfo.setText(ticketDetails(dh.getPaHtml()));
        // nastavim cenu
        priceInfo.setText("Celková cena s DPH: "+ ticketPrice(dh.getPaHtml()));


        Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dh.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/personalData.xhtml");
                dh.setPaPOSTdata(createPOSTData(dh.getPaHtml()));
                POSTDataSync ret = new POSTDataSync();
                dh.setPaHtml(ret.POSTDataSyncFunc(dh));

                Intent activityChangeIntent = new Intent(SelectPassengerInfoActivity.this, SelectFinishPayActivity.class);
                SelectPassengerInfoActivity.this.startActivity(activityChangeIntent);
            }

        });
    }


    private String ticketPrice(String html){
        Document doc = Jsoup.parse(html);
        return doc.getElementById("personalData:priceContainer").select("div > p > strong").html();
    }


    private String ticketDetails(String html){
        Document doc = Jsoup.parse(html);
        Elements detailData =doc.select(".tmp-shopping-detail-data");
        Elements journeyDetail = detailData.select("div > ul");
        return journeyDetail.first().children().html();

    }



    @Override
    public String createPOSTData(String html) {
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
                    Uri.encode("personalData:payerItemsList:0:field")+ "=" + Uri.encode(person.getEmail())+"&"+
                    Uri.encode("personalData:shoppingCartItemList:0:travellerItemsList:0:field")+ "=" + Uri.encode(person.getName())+"&"+
                    Uri.encode("personalData:shoppingCartItemList:0:travellerItemsList:1:fieldRegId")+ "=" + Uri.encode(person.getSurname())+"&"+
                    Uri.encode("personalData:shoppingCartItemList:0:travellerItemsList:2:field")+ "=" + Uri.encode(person.getIDcard())+"&"+
                    Uri.encode("personalData:shoppingCartItemList:0:travellerItemsList:3:fieldRegId")+ "=" + Uri.encode(person.getRegNumber())+"&"+
                    Uri.encode("personalData:personalDataAgreement")+ "=" + "on&"+                  //suhlasim s podmienkami
                    "javax.faces.ViewState"+"="+Uri.encode(value)+"&"+
                    Uri.encode(personalData,"utf-8") + "="+Uri.encode(personalData,"utf-8");        //ticket data
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";
    }


}
