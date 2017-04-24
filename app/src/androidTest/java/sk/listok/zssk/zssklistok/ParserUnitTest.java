package sk.listok.zssk.zssklistok;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sk.listok.zssk.zssklistok.helpers.IParserPostData;
import sk.listok.zssk.zssklistok.helpers.PostCreator;

import static org.junit.Assert.assertEquals;

/**
 * Unit testy na všetky funkcie parsera.
 */
@RunWith(AndroidJUnit4.class)
public class ParserUnitTest {
    private Context c;
    private IParserPostData parser = new PostCreator();


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        this.c = InstrumentationRegistry.getTargetContext();
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("sk.listok.zssk.zssklistok", appContext.getPackageName());
    }


    private String loadTestFile(String fileName) {
        this.c = InstrumentationRegistry.getTargetContext();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(c.getAssets().open("testdata/" + fileName)));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                sb.append(mLine);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return sb.toString();
    }


    @Test
    public void testPostFindTrains() {
        String result = parser.postFindTrains("Zvolen", "Žilina", "22:03", "24.4.2017");
        Assert.assertEquals("lang=sk&portal=&from=Zvolen&to=%C5%BDilina&via=&date=24.4.2017&time=22%3A03&departure=true", result);
    }

    @Test
    public void testPostSelectTrain() {
        String result = parser.postSelectTrain(loadTestFile("postSelectTrain.txt"), "searchForm:inetConnection_:1:j_idt463");
        String correct = "searchForm=searchForm&javax.faces.ViewState=371866678870532007%3A-6429453143926277954&searchForm%3AinetConnection_%3A1%3Aj_idt463=searchForm%3AinetConnection_%3A1%3Aj_idt463";
        Assert.assertEquals(correct, result);
    }

    @Test
    public void testPostTicketType() {
        String result = parser.postTicketType(loadTestFile("postTicketType.txt"), 2);
        String correct = "ticketParam=ticketParam&ticketParam%3Apassenger%3A0%3Aj_idt117=2&ticketParam%3Apassenger%3A0%3AcontingentCheck=on&javax.faces.ViewState=1454431919705269996%3A5633326681237997671&ticketParam%3Aj_idt603=ticketParam%3Aj_idt603";
        Assert.assertEquals(correct, result);
    }

    @Test
    public void testTicketDetails() {
        String result = parser.ticketDetails(loadTestFile("ticketDetails.txt"));
        String correct = "Zvolen os.st. - Banská Bystrica, 21 km, 2.tr., NO, Os 7308, 07:00 - 07:31\n" +
                "Banská Bystrica - Žilina, 96 km, 2.tr., NO, R 942 Turiec, 07:34 - 09:06<br>Cest. lístok platí len s prísluš.preukazom.\n" +
                "Nástup cesty možný len v 1.deň platnosti. Bezplatný CL a miestenka cez t.č. 18 188<br>";
        Assert.assertEquals(correct, result);
    }

    @Test
    public void testTicketPrice() {
        String result = parser.ticketPrice(loadTestFile("ticketDetails.txt"));
        String correct = "0.00 EUR";
        Assert.assertEquals(correct, result);
    }


    @Test
    public void testPostPassengerInfo() {
        String result = parser.postPassengerInfo(loadTestFile("passengerInfo.txt"), "email.test@gmail.com", "Dávid", "Priezvisko", "0001111");
        String correct = "ticketParam=ticketParam&personalData=personalData&personalData%3ApayerItemsList%3A0%3Afield=email.test%40gmail.com&personalData%3AshoppingCartItemList%3A0%3AtravellerItemsList%3A0%3Afield=D%C3%A1vid&personalData%3AshoppingCartItemList%3A0%3AtravellerItemsList%3A1%3AfieldRegId=Priezvisko&personalData%3AshoppingCartItemList%3A0%3AtravellerItemsList%3A2%3AfieldRegId=0001111&javax.faces.ViewState=2947417884276672485%3A-6337867539307228287&personalData%3Aj_idt334=personalData%3Aj_idt334";
        Assert.assertEquals(correct, result);
    }

    @Test
    public void testCheckUserInfo() {
        String result = parser.checkUserInfo(loadTestFile("checkUserInfo.txt"));
        String correct = "";
        Assert.assertEquals(correct, result);
    }
}
