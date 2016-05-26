package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private appView appV;
    private MainActivity activity;
    Games games;

    private LinearLayout linLayout;
    private static Context context;
    private Resources resources;
    private String output;
    Spinner spinner;

    private ArrayList<String> gameNames = new ArrayList<>();
    private String selectedGame = null;
    private ArrayList<Button> buttonsP = new ArrayList<>();
    private ArrayList<Button> buttonsM = new ArrayList<>();
    private ArrayList<TextView> trackVal = new ArrayList<>();
    ArrayList<Pair<String, CounterBuilder>> list = null;

    private ArrayList<String> vals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);

        resources = getResources();
        activity = this;

        getGames();
        appV = appView.getInstance(activity);
    }

    private void getGames(){
        try {
            new Thread(new Runnable(){
                public void run() {
                Looper.prepare();
                    System.out.println("games loading");         //debug
                try{
                    output = LoadFile("gamelist");
                    Log.i("test", output);
                }
                catch(IOException e){
                    Toast toast = Toast.makeText(getApplicationContext(), "File: not found", Toast.LENGTH_LONG);
                    toast.show();
                }

                try{
                    BufferedReader br = new BufferedReader(new StringReader(output));
                    String line;
                    ArrayList<Game> gamesList = new ArrayList<>();
                    while ((line = br.readLine()) != null) {
                        Game game = new Game(line);
                        //System.out.println(game.getName());     //debug
                        gamesList.add(game);
                        gameNames.add(game.getName());
                    }
                    games.getInstance().addGames(gamesList,gameNames);
                    System.out.println("games loaded");         //debug
                    setSpinner();
                }
                catch(IOException ex){System.out.println("buff, "+ex);}

                }
            }).start();

        }
        catch(Exception e){}
    }

    private String LoadFile(String fileName) throws IOException {
        InputStream iS;

        //get the resource id fromthe file name
        //System.out.println("paketens namn: "+this.getPackageName());
        int rID = this.getResources().getIdentifier("raw/"+fileName, "raw", this.getPackageName());
        //get the file as a stream
        iS = this.getResources().openRawResource(rID);

        //create a buffer that has the same size as the InputStream
        byte[] buffer = new byte[iS.available()];
        //read the text file as a stream, into the buffer
        iS.read(buffer);
        //create a output stream to write the buffer into
        ByteArrayOutputStream oS = new ByteArrayOutputStream();
        //write this buffer to the output stream
        oS.write(buffer);
        //Close the Input and Output streams
        oS.close();
        iS.close();

        //return the output stream as a String
        return oS.toString();
    }

    private void setSpinner(){
        try {
            System.out.println("setting spinners"); //debug
            spinner = (Spinner) activity.findViewById(R.id.spinner);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_spinner_item, gameNames);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
            System.out.println("spinners set"); //debug
        }
        catch(Exception ex){System.out.println("spinner, "+ex);}
    }

    private void buildRows(String game){
        linLayout = (LinearLayout) findViewById(R.id.linLayout);
        linLayout.removeAllViewsInLayout();
        if(list != null){
            buttonsP.clear();
            buttonsM.clear();
            trackVal.clear();
        }
        list = games.getInstance().getGamesList().get(gameNames.indexOf(game)).getList();

        System.out.println("tracker list size: "+list.size()); //debug
        for (int cTrack = 0; cTrack<list.size();cTrack++) {
            try {
               /* new Thread(new Runnable() {
                    public void run() {
                        Looper.prepare();*/
                        System.out.println("setting tracker " + cTrack); //debug
                        LinearLayout lin = new LinearLayout(context);
                        lin.setOrientation(LinearLayout.HORIZONTAL);

                        TextView text = new TextView(context);
                        text.setText(list.get(cTrack).first);
                        text.setTextSize(18);
                        text.setWidth(250);
                        text.setPadding(25, 0, 0, 0);
                        lin.addView(text);

                        Button btnMin = new Button(context);
                        btnMin.setText("-");
                        btnMin.setTextSize(18);
                        btnMin.setMaxWidth(50);
                        btnMin.setOnClickListener(new ButtonPressedListener());
                        buttonsM.add(btnMin);
                        lin.addView(btnMin);
                        //btnMin.setLayoutParams(new LinearLayout.LayoutParams(50, 40));

                        TextView value = new TextView(context);
                        value.setText(list.get(cTrack).second.getDefault());
                        value.setTextSize(18);
                        value.setWidth(200);
                        value.setPadding(25, 0, 0, 0);
                        trackVal.add(value);
                        lin.addView(value);

                        Button btnPl = new Button(context);
                        btnPl.setText("+");
                        btnPl.setTextSize(18);
                        btnPl.setMaxWidth(50);
                        btnPl.setOnClickListener(new ButtonPressedListener());
                        buttonsP.add(btnPl);
                        lin.addView(btnPl);
                        //btnPl.setLayoutParams(new LinearLayout.LayoutParams(50, 40));

                        linLayout.addView(lin);
                        System.out.println("tracker " + cTrack + " set up with size " + list.get(cTrack).second.getCounterValues().size()); //debug
                 /*   }
                }).start();*/
            }
            catch(NullPointerException n){

            }/*
            try {
                synchronized (this) {
                    wait(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();

            if(selectedGame != null){
                Toast.makeText(parent.getContext(), "Avataan " + selectedItem,
                        Toast.LENGTH_SHORT).show();
            }
            selectedGame = selectedItem;
            buildRows(selectedItem);
            //appV.buildRows(selectedItem);

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    public class ButtonPressedListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View v) {
            if(buttonsM.contains(v)) {
                int i = buttonsM.indexOf(v);
                try {
                    if(list.get(i).second.getCounterType()==0) {
                        int j = list.get(i).second.getCounterValues().indexOf(trackVal.get(i).getText());
                        trackVal.get(i).setText(list.get(i).second.getCounterValues().get(j-1).toString());
                    }
                    else if(list.get(i).second.getCounterType()==1) {
                        int calc = Integer.parseInt(trackVal.get(i).getText().toString()) -1;
                        int min = (Integer) list.get(i).second.getCounterValues().get(1);
                        //System.out.println(list.get(i).first+": now="+calc+" min="+min);    //debug
                        if(calc<min)
                            throw new Exception();
                        else trackVal.get(i).setText(String.valueOf(calc));
                    }
                } catch (IndexOutOfBoundsException n) {
                    Log.i("test", "Could not subtract from "+list.get(i).first+". "+n);
                } catch (Exception e){
                    Log.i("test", "Could not subtract from "+list.get(i).first+". "+e);
                }
            }
            else{
                int i = buttonsP.indexOf(v);
                try{
                    if(list.get(i).second.getCounterType()==0) {
                        int j = list.get(i).second.getCounterValues().indexOf(trackVal.get(i).getText());
                        trackVal.get(i).setText(list.get(i).second.getCounterValues().get(j+1).toString());
                    }
                    else if(list.get(i).second.getCounterType()==1) {
                        int calc = Integer.parseInt(trackVal.get(i).getText().toString()) +1;
                        int max = (Integer) list.get(i).second.getCounterValues().get(2);
                        //System.out.println(list.get(i).first+": now="+calc+" max="+max);    //debug
                        if(calc>max)
                            throw new Exception();
                        else trackVal.get(i).setText(String.valueOf(calc));
                    }
                }
                catch(IndexOutOfBoundsException n){
                    Log.i("test", "Could not add to "+list.get(i).first+". "+n);
                } catch (Exception e){
                    Log.i("test", "Could not add to "+list.get(i).first+". "+e);
                }
            }
            for(int i=0;1<trackVal.size()-1;i++)
            vals.add(trackVal.get(i).toString());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            System.out.println("rotatiooooooon!!!");
            //spinner.setSelection(gameNames.indexOf(selectedGame));
            Toast.makeText(context, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            System.out.println("rotatiooooooon!!!");
            Toast.makeText(context, "portrait", Toast.LENGTH_SHORT).show();
            //spinner.setSelection(gameNames.indexOf(selectedGame));
        }
    }
}
