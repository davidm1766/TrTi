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

import sk.listok.zssk.zssklistok.sharedData.DataHolder;
import sk.listok.zssk.zssklistok.sharedData.IPostable;

public class SelectFinishPayActivity extends AppCompatActivity implements IPostable {

    private DataHolder dh = DataHolder.getInst();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_finish_pay);


        Button button = (Button) findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dh.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/payment.xhtml");
                dh.setPaPOSTdata(createPOSTData(dh.getPaHtml()));
                POSTDataSync ret = new POSTDataSync();
                dh.setPaHtml(ret.POSTDataSyncFunc(dh));

                Intent activityChangeIntent = new Intent(SelectFinishPayActivity.this, MainActivity.class);
                SelectFinishPayActivity.this.startActivity(activityChangeIntent);
            }

        });


    }

    @Override
    public String createPOSTData(String html) {
        try {
            Document doc = Jsoup.parse(html);


            Element btn = doc.getElementsContainingOwnText("Pokračovať v nákupe").last();
            String selectedLink = btn.attr("onClick");
            int start = selectedLink.indexOf("paymentForm:");
            int end = selectedLink.indexOf('\\',start);
            int count = end  - start;
            String paymentForm = selectedLink.substring(start,end);

            Element javaView = doc.getElementById("javax.faces.ViewState");
            String value = javaView.attr("value");

            System.out.println("+++++++++++++++"+ value +"+++++++++++++++++++");

            String finalstr =
                    "paymentForm=paymentForm&"+
                            URLEncoder.encode("paymentForm:SendConfirmation","utf-8")+ "=" + URLEncoder.encode("on","utf-8")+"&"+
                            URLEncoder.encode("paymentForm:PersonalDataFormDirect","utf-8")+ "=" + URLEncoder.encode("on","utf-8")+"&"+
                            "javax.faces.ViewState"+"="+URLEncoder.encode(value,"utf-8")+"&"+
                            URLEncoder.encode(paymentForm,"utf-8") + "="+URLEncoder.encode(paymentForm,"utf-8");
            return  finalstr;
        } catch (Exception e){
            System.out.println("CHYBA:"+e.toString());

        }
        return "";
    }


}
