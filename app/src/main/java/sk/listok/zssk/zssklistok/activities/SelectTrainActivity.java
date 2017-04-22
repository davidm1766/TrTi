package sk.listok.zssk.zssklistok.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
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

import sk.listok.zssk.zssklistok.MainActivity;
import sk.listok.zssk.zssklistok.communication.INotifyDownloader;
import sk.listok.zssk.zssklistok.communication.INotifyParser;
import sk.listok.zssk.zssklistok.communication.Provider;
import sk.listok.zssk.zssklistok.helpers.ErrorHelper;
import sk.listok.zssk.zssklistok.objects.JourneyData;
import sk.listok.zssk.zssklistok.R;
import sk.listok.zssk.zssklistok.objects.TrainData;
import sk.listok.zssk.zssklistok.helpers.ParserFoundTrains;
import sk.listok.zssk.zssklistok.communication.DataHolder;

public class SelectTrainActivity extends AppCompatActivity implements View.OnClickListener,INotifyParser,INotifyDownloader {

    private ArrayList<JourneyData> journeyData;
    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_train);
        clicked = false;
        loadTrains();
    }

    @Override
    public void onBackPressed() {
        Provider.Instance(this).peekDataHolder();
        super.onBackPressed();
    }

    /**
     * Funkcia nacita vlaky. Pri nacitani sa
     * pouzije parser, ktory pracuje asynchronne.
     */
    private void loadTrains(){
        new ParserFoundTrains(this).execute(Provider.getDataholder().getPaHtml());
    }

    /**
     * Vykresli gridy do tabulky - jeden riadok v tabulke
     * moze obsahovat viacero gridov, lebo grid predstavuje
     * jeden prestup.
     * @param tr Jecnotlive cesty, pricom kazda cesta moze predstavovat viacero
     *           spojov.
     */
    private void createGrids(ArrayList<JourneyData> tr) {

        int rowID = 0;
        for(JourneyData jd : tr) {
            addGridToTable(jd,rowID++);
        }
        if(tr.size()==0){
            //ak nemam ziadne data tak zobrazim hlasku ze sa nenasli spoje
            TextView tv = (TextView) findViewById(R.id.textViewTrainsNotFound);
            tv.setVisibility(View.VISIBLE);
        } else {
            TextView tv = (TextView) findViewById(R.id.textViewTrainsNotFound);
            tv.setVisibility(View.INVISIBLE);
        }

    }



    /**
     * Funkcia prida cely grid do tabulky, kde jeden riadok predstavuje
     * jednu cestu z mesta x do mesta y a moze byt na nej aj niekolko
     * prestupov - stale je to jeden riadok v tabulke...
     * @param jd Jedna konkrétna cesta
     * @param rowID ID riadku (v poradi)
     */
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
        tw.setText(setBold(getString(R.string.TOTAL_TIME)+jd.getJourneyTime()));
        tw.setPadding(0,0,20,0);
        tw.setLayoutParams(lp);

        TableRow totalTime = new TableRow(this);
        totalTime.addView(tw);
        tl.addView(totalTime);

        tr.addView(tl);
    }


    /**
     * Vrati string s BOLD.
     * @param s Retazec, ktorý sa zmeni na bold.
     */
    private SpannableString setBold(String s){
        SpannableString spanString = new SpannableString(s);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        return spanString;
    }

    /**
     * Vrati string s ITALIC.
     * @param s Retazec, ktorý sa zmení na italic.
     */
    private SpannableString setItalic(String s){
        SpannableString spanString = new SpannableString(s);
        spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
        return spanString;
    }

    /**
     * Vygeneruje 1 grid, ktory predstavuje cestu jednym vlakom.
     * @param td Informacie o jednej ceste.
     * @param yellowTrain, či bude vlak žltý alebo nie.
     */
    private GridLayout generateGridForTrain(TrainData td, boolean yellowTrain) {

        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(6);
        gridLayout.setRowCount(8);

        // pri uprave poctu prvkov v gride treba upravit layout params
        View cells[] = new View[10];

        cells[0] = new ImageView(this);
        if (yellowTrain) {
            ((ImageView)cells[0]).setImageResource(R.drawable.trainicored);
        }else {
            ((ImageView)cells[0]).setImageResource(R.drawable.trainicowhite);
        }

        cells[1] = new TextView(this);
        String trainName = td.getNameTrain();
        ((TextView)cells[1]).setText(setBold(trainName.substring(0,trainName.indexOf('('))));//"R 831 TAJOV"));
        cells[2] = new TextView(this);
        ((TextView)cells[2]).setText(setItalic(td.getFromTown() +" -> "+td.getToTown()));//"Zvolen -> Banská Bystrica"); td.getFromTown()
        cells[3] = new TextView(this);
        ((TextView)cells[3]).setText(R.string.DEPARTURE);
        cells[4] = new TextView(this);
        ((TextView)cells[4]).setText(setBold(td.getDepartueTime()));//setBold("11:00"));
        cells[5] = new TextView(this);
        ((TextView)cells[5]).setText(td.getDepartueDate());//"01.03.2017");
        cells[6] = new TextView(this);
        ((TextView)cells[6]).setText(R.string.ARRIVAL);
        cells[7] = new TextView(this);
        ((TextView)cells[7]).setText(setBold(td.getArrivalTime()));//setBold("13:02"));
        cells[8] = new TextView(this);
        ((TextView)cells[8]).setText(td.getArrivalDate());//"01.03.2017");
        cells[9] = new TextView(this); //medzera


        GridLayout.LayoutParams[] glp = generateLayoutParams();
        int i = 0;
        for(View v : cells){
            v.setLayoutParams(glp[i++]);
            if(v instanceof TextView){
                //pri pouzivat api 15 to inak neporiesim iba cez depricated
                ((TextView)v).setTextAppearance(this,android.R.style.TextAppearance_Medium);
            }
            gridLayout.addView(v);
        }

        return gridLayout;
    }


    /**
     *  Funkcia vrati layout params pre kazdy prvok v gride,
     *  pocet layoutparams sa musi zhodovat s poctom prvkov v gride!!!
     */
    private GridLayout.LayoutParams[] generateLayoutParams(){
        GridLayout.LayoutParams[] glp = new GridLayout.LayoutParams[10];

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth;
        int screenHeight;

        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels) {
            //landscape - na sirku
            screenWidth = size.y;
            screenHeight = size.x;
        } else {
            //portait - na vysku
            screenWidth = size.x;
            screenHeight = size.y;
        }

        int halfScreenWidth = (int)(screenWidth *0.5);
        int quarterScreenWidth = (int)(halfScreenWidth * 0.5);
        int rowSize = 30;


        glp[0] = new GridLayout.LayoutParams();
        glp[0].height = screenHeight / rowSize*2;
        glp[0].width = screenWidth / 6;
        glp[0].setMargins(40,15,0,10);
        glp[0].setGravity(Gravity.CENTER);
        glp[0].rowSpec = GridLayout.spec(1,2);
        glp[0].columnSpec = GridLayout.spec(0,0);


        glp[1] = new GridLayout.LayoutParams();
        glp[1].height = screenHeight / rowSize;
        glp[1].width = screenWidth * 5 / 6 ;
        glp[1].setGravity(Gravity.LEFT);
        glp[1].columnSpec = GridLayout.spec(1,4);
        glp[1].rowSpec = GridLayout.spec(1);


        glp[2] = new GridLayout.LayoutParams();
        glp[2].height = screenHeight / rowSize;
        glp[2].width = screenWidth * 5/6 ;
        glp[2].setGravity(Gravity.LEFT);
        glp[2].columnSpec = GridLayout.spec(1,4);
        glp[2].rowSpec = GridLayout.spec(2);


        glp[3] = new GridLayout.LayoutParams();
        glp[3].height = screenHeight / rowSize;
        glp[3].width = screenWidth / 4 ;
        glp[3].setGravity(Gravity.LEFT);
        glp[3].leftMargin = 55;
        glp[3].columnSpec = GridLayout.spec(0,1);
        glp[3].rowSpec = GridLayout.spec(3);


        glp[4] = new GridLayout.LayoutParams();
        glp[4].height = screenHeight / rowSize;
        glp[4].width = screenWidth / 4 ;
        glp[4].setGravity(Gravity.LEFT);
        glp[4].columnSpec = GridLayout.spec(1,1);
        glp[4].rowSpec = GridLayout.spec(3);

        glp[5] = new GridLayout.LayoutParams();
        glp[5].height = screenHeight / rowSize;
        glp[5].width = screenWidth / 4*2 ;
        glp[5].setGravity(Gravity.LEFT);
        glp[5].columnSpec = GridLayout.spec(2,1);
        glp[5].rowSpec = GridLayout.spec(3);


        glp[6] = new GridLayout.LayoutParams();
        glp[6].height = screenHeight / rowSize;
        glp[6].width = screenWidth / 4 ;
        glp[6].setGravity(Gravity.LEFT);
        glp[6].leftMargin = 55;
        glp[6].columnSpec = GridLayout.spec(0,1);
        glp[6].rowSpec = GridLayout.spec(4);

        glp[7] =new GridLayout.LayoutParams();
        glp[7].height = screenHeight / rowSize;
        glp[7].width = screenWidth / 4 ;
        glp[7].setGravity(Gravity.LEFT);
        glp[7].columnSpec = GridLayout.spec(1,1);
        glp[7].rowSpec = GridLayout.spec(4);

        glp[8] =new GridLayout.LayoutParams();
        glp[8].height = screenHeight / rowSize;
        glp[8].width = screenWidth / 4*2 ;
        glp[8].setGravity(Gravity.LEFT);
        glp[8].columnSpec = GridLayout.spec(2,1);
        glp[8].rowSpec = GridLayout.spec(4);


        glp[9] =new GridLayout.LayoutParams();
        glp[9].height = screenHeight / (rowSize);
        glp[9].width = screenWidth;
        glp[9].rowSpec = GridLayout.spec(6);
        return glp;
    }


    @Override
    public void onClick(View v) {
        if(!clicked) {
            clicked = true;
            String id = journeyData.get(v.getId()).getIdJourney();
            String toSend = Provider.getIParerInstance(this).postSelectTrain(Provider.getDataholder().getPaHtml(), id);
            if(toSend == null || toSend.equals("")){
                ErrorHelper.onError(this);
                return;
            }
            Provider.Instance(SelectTrainActivity.this).doRequest("https://ikvc.slovakrail.sk/inet-sales-web/pages/connection/search.xhtml",
                    toSend);
        }
    }


    @Override
    public void parsered(ArrayList<JourneyData> data) {
        //prislo mi to rozpasovane...
        this.journeyData = data;
        createGrids(this.journeyData);
    }


    @Override
    public void downloaded(DataHolder dh) {
        Intent activityChangeIntent = new Intent(SelectTrainActivity.this,SelectPassengerTypeActivity.class);
        SelectTrainActivity.this.startActivity(activityChangeIntent);
        clicked = false;
    }

    @Override
    public Context getContext() {
        return this;
    }
}