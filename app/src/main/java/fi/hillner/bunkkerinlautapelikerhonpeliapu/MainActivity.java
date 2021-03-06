package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import android.content.Context;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private static final String TAG = "MainActivity";

    private LinearLayout linLayout;
    private static Context context;
    private String output;
    Spinner spinner;

    private ArrayList<String> gameNames;
    private String selectedGame = null;
    private ArrayList<Button> buttonsP = new ArrayList<>();
    private ArrayList<Button> buttonsM = new ArrayList<>();
    private ArrayList<TextView> trackVal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);

        activity = this;

        getGames();
        appV = appView.getInstance(activity);
    }

    private void getGames(){
        Log.i(TAG, "Application started");
        try {
            new Thread(new Runnable(){
                public void run() {
                    Looper.prepare();
                    Log.i(TAG, "Loading games");         //debug
                    try{
                        output = LoadFile("gamelist");
                        //Log.d(TAG, output);        //debug
                    }
                    catch(IOException e){
                        Toast toast = Toast.makeText(getApplicationContext(), "File: not found", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    ArrayList<Game> gamesList = new ArrayList<>();
                    gameNames = new ArrayList<>();
                    try{
                        BufferedReader br = new BufferedReader(new StringReader(output));
                        String line;

                        while ((line = br.readLine()) != null) {
                            Log.d(TAG, "Adding new game"); //debug

                            Game game = new Game(line, activity);
                            gamesList.add(game);
                            gameNames.add(game.getName());
                            Log.d(TAG, "Game added: '"+game.getName()+"'"); //debug
                        }
                        games = new Games();
                        games.addGames(gamesList,gameNames);
                        Log.i(TAG, "Number of games loaded: "+games.getGamesList().size());
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

        //get the resource id from the file name
        //System.out.println("package name: "+this.getPackageName());
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
            Log.i(TAG, "Setting spinner");
            spinner = (Spinner) activity.findViewById(R.id.spinner);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_spinner_item, gameNames);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setPrompt("Choose a game");
            spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
            Log.i(TAG, "Spinner set");
        }
        catch(Exception ex){System.out.println("Spinner exception:\n"+ex);}
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            linLayout = (LinearLayout) findViewById(R.id.linLayout);
            linLayout.removeAllViewsInLayout();

            String selectedItem = parent.getItemAtPosition(pos).toString();
            //System.out.println("nameslist: "+gameNames.size()+"\ngamelist: "+games.getGamesList().size()); //debug
            if(games.getGamesList().size()!=0 && !selectedItem.equals("Choose a game") && !selectedItem.equals(null)) {

                if(selectedGame != null){
                    Toast.makeText(parent.getContext(), "Opening " + selectedItem,
                            Toast.LENGTH_SHORT).show();
                }
                selectedGame = selectedItem;

                buttonsP.clear();
                buttonsM.clear();
                trackVal.clear();

                Log.i(TAG, "Opening '"+selectedItem+"'");


                /*
                Log.d(TAG, "***debug***");
                Log.d(TAG, "selectedItem : "+selectedItem);
                Log.d(TAG, "gameNames.indexOf(selectedItem) : "+gameNames.indexOf(selectedItem));
                Log.d(TAG, "games.getGamesList().get(gameNames.indexOf(selectedItem)) : "+games.getGamesList().get(gameNames.indexOf(selectedItem)));
                Log.d(TAG, "Num rows: "+games.getGamesList().get(gameNames.indexOf(selectedItem)).getList().size());
                Log.d(TAG, "appV.getRows(games.getGamesList().get(gameNames.indexOf(selectedItem))) : "+appV.getRows(games.getGamesList().get(gameNames.indexOf(selectedItem))));
                Log.d(TAG, "***debug end***");
                */
                ArrayList<LinearLayout> temp = appV.getRows(games.getGamesList().get(gameNames.indexOf(selectedItem)));
                for(int i=0;i<temp.size();i++){
                    linLayout.addView(temp.get(i));
                }
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
}
