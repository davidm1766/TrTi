package sk.listok.zssk.zssklistok;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    Context c;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        this.c = InstrumentationRegistry.getTargetContext();
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("sk.listok.zssk.zssklistok", appContext.getPackageName());
    }


    private String loadTestFile(String fileName){
        this.c = InstrumentationRegistry.getTargetContext();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(c.getAssets().open("testdata/"+fileName)));

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
    public void noMoreTickets(){
        String html = loadTestFile("NoMoreTicket.txt");

        Document doc = Jsoup.parse(html);
        Elements allLists = doc.select(".tmp-anulation-form-false");



    }


}
