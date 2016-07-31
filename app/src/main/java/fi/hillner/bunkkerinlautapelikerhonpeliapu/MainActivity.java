package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Looper;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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

    private LinearLayout linLayout;
    private static Context context;
    private String output;
    Spinner spinner;

    private ArrayList<String> gameNames;
    private String selectedGame = null;
    private ArrayList<Button> buttonsP = new ArrayList<>();
    private ArrayList<Button> buttonsM = new ArrayList<>();
    private ArrayList<TextView> trackVal = new ArrayList<>();
    ArrayList<Pair<String, CounterBuilder>> list = null;

    private RetainedFragment dataFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);

        activity = this;

        getGames();
        appV = appView.getInstance(activity);

        // find the retained fragment on activity restarts
        fm = getFragmentManager();
        dataFragment = (RetainedFragment) fm.findFragmentByTag("games");

        // create the fragment and data the first time
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment, "games").commit();
            // you can now load the data
        }
        // the data is available in dataFragment.getData()

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveView();
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveView();
    }

    private void saveView(){
        String[] values = new String[trackVal.size()];
        for(int i=0;i<trackVal.size();i++){
            values[i] = trackVal.get(i).getText().toString();
        }
        dataFragment.setData(selectedGame, values);
    }

    private void getGames(){
        try {
            new Thread(new Runnable(){
                public void run() {
                    Looper.prepare();
                        System.out.println("Loading games");         //debug
                    try{
                        output = LoadFile("gamelist");
                        //Log.i("test", output);
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
                            Game game = new Game(line, activity);
                            gamesList.add(game);
                            gameNames.add(game.getName());
                            System.out.println("Game added: '"+game.getName()+"'"); //debug
                        }
                        games = new Games();
                        games.addGames(gamesList,gameNames);
                        System.out.println("Number of games loaded: "+games.getGamesList().size());
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
            System.out.println("Setting spinner");
            spinner = (Spinner) activity.findViewById(R.id.spinner);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_spinner_item, gameNames);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setPrompt("Choose a game");
            spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
            System.out.println("Spinner set");
        }
        catch(Exception ex){System.out.println("Spinner exception:\n"+ex);}
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            linLayout = (LinearLayout) findViewById(R.id.linLayout);
            linLayout.removeAllViewsInLayout();

            if(selectedGame!=null) {
                saveView();
                fm.beginTransaction().addToBackStack("games").commit();
            }
            String selectedItem = parent.getItemAtPosition(pos).toString();
            //System.out.println("nameslist: "+gameNames.size()+"\ngamelist: "+games.getGamesList().size()); //debug
            if(games.getGamesList().size()!=0 && !selectedItem.equals("Choose a game") && !selectedItem.equals(null)) {

                if(selectedGame != null){
                    Toast.makeText(parent.getContext(), "Avataan " + selectedItem,
                            Toast.LENGTH_SHORT).show();
                }
                selectedGame = selectedItem;

                buttonsP.clear();
                buttonsM.clear();
                trackVal.clear();

                System.out.println("Opening '"+selectedItem+"'");


                /*
                //debug
                System.out.println("***debug***");
                System.out.println("selectedItem : "+selectedItem);
                System.out.println("gameNames.indexOf(selectedItem) : "+gameNames.indexOf(selectedItem));
                System.out.println("games.getGamesList().get(gameNames.indexOf(selectedItem)) : "+games.getGamesList().get(gameNames.indexOf(selectedItem)));
                System.out.println("Num rows: "+games.getGamesList().get(gameNames.indexOf(selectedItem)).getList().size());
                System.out.println("appV.getRows(games.getGamesList().get(gameNames.indexOf(selectedItem))) : "+appV.getRows(games.getGamesList().get(gameNames.indexOf(selectedItem))));
                System.out.println("***debug end***");
                */
                ArrayList<LinearLayout> temp = appV.getRows(games.getGamesList().get(gameNames.indexOf(selectedItem)));
                for(int i=0;i<temp.size();i++){
                    linLayout.addView(temp.get(i));
                }
            }


            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment, "games").commit();
            saveView();
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        dataFragment = (RetainedFragment) fm.findFragmentByTag("games");

        if (dataFragment == null) {
            // add the fragment
            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment, "games").commit();
            // you can now load the data
        }

        String[] temp = dataFragment.getData();
        int j;
        for(j=0; j<gameNames.size();j++){
            if(gameNames.get(j).equals(temp[0])) break;
        }
        if(j<gameNames.size()){
            spinner.setSelection(j);
            selectedGame = temp[j];
        }

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
