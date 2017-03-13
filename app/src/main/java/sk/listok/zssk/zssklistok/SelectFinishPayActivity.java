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

public class SelectFinishPayActivity extends AppCompatActivity implements IPharseableHTML {


    private HttpObject ht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_finish_pay);
        ht = (HttpObject) getIntent().getExtras().getParcelable("HttpObject");

        Button button = (Button) findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ht.setPaUrl("https://ikvc.slovakrail.sk/inet-sales-web/pages/shopping/payment.xhtml");
       //         ht.setPaPOSTdata(parse(ht.getPaHtml()));
           //     ht.setPaHtml(POSTdata(ht)); // toto treba opravit a odkomentit

                Intent activityChangeIntent = new Intent(SelectFinishPayActivity.this, MainActivity.class);
                activityChangeIntent.putExtra("HttpObject",ht);
                SelectFinishPayActivity.this.startActivity(activityChangeIntent);
            }

        });


    }

    @Override
    public String parse(String html) {
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

    @Override
    public void POSTdata(HttpObject ht) {
      // new POSTData().execute(ht);

    }
}
