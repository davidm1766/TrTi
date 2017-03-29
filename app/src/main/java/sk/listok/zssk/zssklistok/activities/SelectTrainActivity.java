package sk.listok.zssk.zssklistok.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;

import sk.listok.zssk.zssklistok.Communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.Communication.INotifyParser;
import sk.listok.zssk.zssklistok.helpers.PostDataCreator;
import sk.listok.zssk.zssklistok.Communication.Provider;
import sk.listok.zssk.zssklistok.JourneyData;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.SelectPassengerTypeActivity;
import sk.listok.zssk.zssklistok.TrainData;
import sk.listok.zssk.zssklistok.helpers.ParserFoundTrains;
import sk.listok.zssk.zssklistok.sharedData.DataHolder;

public class SelectTrainActivity extends AppCompatActivity implements View.OnClickListener,INotifyParser,INotifyDownloader {

    //private DataHolder dh = DataHolder.getInst();
    private ArrayList<JourneyData> journeyData;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_train);
        loadTrains();
    }


    private void loadTrains(){
        new ParserFoundTrains(this).execute(Provider.getDataholder().getPaHtml());
    }

    //vykresli gridy do tabulky
    private void createGrids(ArrayList<JourneyData> tr) {

        int rowID = 0;
        for(JourneyData jd : tr) {
            addGridToTable(jd,rowID++);
        }
    }


    private void addGridToTable(JourneyData jd, int rowID){


        TableRow.LayoutParams tblLP = new TableRow.LayoutParams();

        tblLP.width = TableRow.LayoutParams.WRAP_CONTENT;
        tblLP.height = TableRow.LayoutParams.MATCH_PARENT;
        tblLP.gravity = Gravity.TOP;


        TableRow tr = null;
        TableLayout tl = new TableLayout(this);
        //nastavnie handlovania clicku
        tl.setId(rowID);
        tl.setOnClickListener(this);

        //nastavim pozadie pre kazdu skupinu vlakov
        if(rowID%2==0) {
            tl.setBackgroundColor(Color.rgb(255,255,255));
        } else{
            tl.setBackgroundColor(Color.rgb(255,218,150));
        }

        tl.setLayoutParams(tblLP);

        for(TrainData td : jd.getTrainData()) {
            GridLayout gl = generateGridForTrain(td,true);
            gl.setLayoutParams(tblLP);

            TableRow ll = new TableRow(this);
            ll.setLayoutParams(tblLP);
            ll.addView(gl);
            tl.addView(ll);
        }


        switch (rowID){
            case 0:
                tr = (TableRow) findViewById(R.id.row1);
                break;
            case 1:
                tr = (TableRow) findViewById(R.id.row2);
                break;
            case 2:
                tr = (TableRow) findViewById(R.id.row3);
                break;
            case 3:
                tr = (TableRow) findViewById(R.id.row4);
                break;
            case 4:
                tr = (TableRow) findViewById(R.id.row5);
                break;
            default:
                return;
        }

        //celkovy cas
        TableRow.LayoutParams lp = new TableRow.LayoutParams();
        lp.width = TableRow.LayoutParams.MATCH_PARENT;
        lp.height = TableRow.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.RIGHT;



        TextView tw = new TextView(this);
        tw.setText(setBold("Celkový čas "+jd.getJourneyTime()));
        tw.setPadding(0,0,20,0);
        tw.setLayoutParams(lp);

        TableRow totalTime = new TableRow(this);
        totalTime.addView(tw);
        tl.addView(totalTime);

        tr.addView(tl);
    }


    private SpannableString setBold(String s){
        SpannableString spanString = new SpannableString(s);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        return spanString;
    }

    private SpannableString setItalic(String s){
        SpannableString spanString = new SpannableString(s);
        spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
        return spanString;
    }

    private GridLayout generateGridForTrain(TrainData td, boolean yellowTrain) {

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int halfScreenWidth = (int)(screenWidth *0.5);
        int quarterScreenWidth = (int)(halfScreenWidth * 0.5);

        GridLayout gridLayout = new GridLayout(this);

        gridLayout.setColumnCount(6);
        gridLayout.setRowCount(8);


        ImageView cell1 = new ImageView(this);
        if (yellowTrain) {
            cell1.setImageResource(R.drawable.trainicored);
        }else {
            cell1.setImageResource(R.drawable.trainicowhite);
        }


        TextView cell2 = new TextView(this);
        String trainName = td.getNameTrain();
        cell2.setText(setBold(trainName.substring(0,trainName.indexOf('('))));//"R 831 TAJOV"));


        TextView cell3 = new TextView(this);
        cell3.setText(setItalic(td.getFromTown() +" -> "+td.getToTown()));//"Zvolen -> Banská Bystrica"); td.getFromTown()

        TextView cell4 = new TextView(this);
        cell4.setText("Odchod ");
        TextView cell5 = new TextView(this);
        cell5.setText(setBold(td.getDepartueTime()));//setBold("11:00"));
        TextView cell6 = new TextView(this);
        cell6.setText(td.getDepartueDate());//"01.03.2017");
        TextView cell7 = new TextView(this);
        cell7.setText("Príchod ");
        TextView cell8 = new TextView(this);
        cell8.setText(setBold(td.getArrivalTime()));//setBold("13:02"));
        TextView cell9 = new TextView(this);
        cell9.setText(td.getArrivalDate());//"01.03.2017");

        TextView cellSpace = new TextView(this);

        int rowSize = 30;

        GridLayout.LayoutParams paramTwoColumns =new GridLayout.LayoutParams();
        paramTwoColumns.height = GridLayout.LayoutParams.WRAP_CONTENT;
        paramTwoColumns.width = GridLayout.LayoutParams.WRAP_CONTENT;
        paramTwoColumns.setGravity(Gravity.CENTER);
        paramTwoColumns.columnSpec = GridLayout.spec(2);


        GridLayout.LayoutParams b1 =new GridLayout.LayoutParams();
        b1.height = screenHeight / rowSize*2;
        b1.width = screenWidth / 6;
        b1.setMargins(40,15,0,10);
        b1.setGravity(Gravity.CENTER);
        b1.rowSpec = GridLayout.spec(1,2);
        b1.columnSpec = GridLayout.spec(0,0);


        GridLayout.LayoutParams b2 =new GridLayout.LayoutParams();
        b2.height = screenHeight / rowSize;
        b2.width = screenWidth * 5 / 6 ;
        b2.setGravity(Gravity.LEFT);
        b2.columnSpec = GridLayout.spec(1,4);
        b2.rowSpec = GridLayout.spec(1);


        GridLayout.LayoutParams b3 =new GridLayout.LayoutParams();
        b3.height = screenHeight / rowSize;
        b3.width = screenWidth * 5/6 ;
        b3.setGravity(Gravity.LEFT);
        b3.columnSpec = GridLayout.spec(1,4);
        b3.rowSpec = GridLayout.spec(2);


        GridLayout.LayoutParams b4 = new GridLayout.LayoutParams();
        b4.height = screenHeight / rowSize;
        b4.width = screenWidth / 4 ;
        b4.setGravity(Gravity.LEFT);
        b4.leftMargin = 55;
        b4.columnSpec = GridLayout.spec(0,1);
        b4.rowSpec = GridLayout.spec(3);


        GridLayout.LayoutParams b5 = new GridLayout.LayoutParams();
        b5.height = screenHeight / rowSize;
        b5.width = screenWidth / 4 ;
        b5.setGravity(Gravity.LEFT);
        b5.columnSpec = GridLayout.spec(1,1);
        b5.rowSpec = GridLayout.spec(3);

        GridLayout.LayoutParams b6 = new GridLayout.LayoutParams();
        b6.height = screenHeight / rowSize;
        b6.width = screenWidth / 4*2 ;
        b6.setGravity(Gravity.LEFT);
        b6.columnSpec = GridLayout.spec(2,1);
        b6.rowSpec = GridLayout.spec(3);


        GridLayout.LayoutParams b7 = new GridLayout.LayoutParams();
        b7.height = screenHeight / rowSize;
        b7.width = screenWidth / 4 ;
        b7.setGravity(Gravity.LEFT);
        b7.leftMargin = 55;
        b7.columnSpec = GridLayout.spec(0,1);
        b7.rowSpec = GridLayout.spec(4);

        GridLayout.LayoutParams b8 =new GridLayout.LayoutParams();
        b8.height = screenHeight / rowSize;
        b8.width = screenWidth / 4 ;
        b8.setGravity(Gravity.LEFT);
        b8.columnSpec = GridLayout.spec(1,1);
        b8.rowSpec = GridLayout.spec(4);

        GridLayout.LayoutParams b9 =new GridLayout.LayoutParams();
        b9.height = screenHeight / rowSize;
        b9.width = screenWidth / 4*2 ;
        b9.setGravity(Gravity.LEFT);
        b9.columnSpec = GridLayout.spec(2,1);
        b9.rowSpec = GridLayout.spec(4);


        GridLayout.LayoutParams bSpace =new GridLayout.LayoutParams();
        bSpace.height = screenHeight / (rowSize);
        bSpace.width = screenWidth;
        bSpace.rowSpec = GridLayout.spec(6);

        cell1.setLayoutParams(b1);
        cell2.setLayoutParams(b2);
        cell3.setLayoutParams(b3);
        cell4.setLayoutParams(b4);
        cell5.setLayoutParams(b5);
        cell6.setLayoutParams(b6);
        cell7.setLayoutParams(b7);
        cell8.setLayoutParams(b8);
        cell9.setLayoutParams(b9);
        cellSpace.setLayoutParams(bSpace);

        int text = android.R.style.TextAppearance_Medium;
       // btn.setTextAppearance(this, text);




        cell2.setTextAppearance(this, text);
        cell3.setTextAppearance(this, text);
        cell4.setTextAppearance(this, text);
        cell5.setTextAppearance(this, text);
        cell6.setTextAppearance(this, text);
        cell7.setTextAppearance(this, text);
        cell8.setTextAppearance(this, text);
        cell9.setTextAppearance(this, text);


/* zafarbi kazdu bunku inak
        btn.setBackgroundColor(Color.rgb(255,0,0));
        btn2.setBackgroundColor(Color.rgb(0,255,0));
        btn3.setBackgroundColor(Color.rgb(0,0,255));
        btn4.setBackgroundColor(Color.rgb(0,0,155));
        btn5.setBackgroundColor(Color.rgb(0,155,0));
        btn6.setBackgroundColor(Color.rgb(155,0,0));
        btn7.setBackgroundColor(Color.rgb(60,60,0));
        btn8.setBackgroundColor(Color.rgb(90,155,155));
        btn9.setBackgroundColor(Color.rgb(0,0,155));


*/
        gridLayout.addView(cell1);
        gridLayout.addView(cell2);
        gridLayout.addView(cell3);
        gridLayout.addView(cell4);
        gridLayout.addView(cell5);
        gridLayout.addView(cell6);
        gridLayout.addView(cell7);
        gridLayout.addView(cell8);
        gridLayout.addView(cell9);
        gridLayout.addView(cellSpace);



        return gridLayout;
    }


    @Override
    public void onClick(View v) {
        String id = journeyData.get(v.getId()).getIdJourney();
        Provider.Instance(SelectTrainActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/connection/search.xhtml",
                PostDataCreator.postSelectTrain(Provider.getDataholder().getPaHtml(),id));
        progressDialog = ProgressDialog.show(SelectTrainActivity.this, "Spracúvavam údaje", "Prosím čakajte...", true);
    }


    @Override
    public void parsered(ArrayList<JourneyData> data) {
        //prislo mi to rozpasovane...
        this.journeyData = data;
        createGrids(this.journeyData);
    }


    @Override
    public void downloaded(DataHolder dh) {
        progressDialog.dismiss();
        //DataHolder.setInst(dh); //LEN KVOLI TESTOVANIU
        Intent activityChangeIntent = new Intent(sk.listok.zssk.zssklistok.activities.SelectTrainActivity.this, SelectPassengerTypeActivity.class);
        SelectTrainActivity.this.startActivity(activityChangeIntent);
    }
}
