package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pat on 11.01.16.
 */
public class Game{
    private String name;

    private static final String TAG = "Game";

    //ArrayList<Pair<String label, Counter values>>
    private ArrayList<Pair<String,Counter>> trackers = new ArrayList<>();

    private static MainActivity activity;
    private static Context context;

    private ArrayList<LinearLayout> linList;
    private LinearLayout lin;
    private ArrayList<Button> buttonsP;
    private ArrayList<Button> buttonsM;
    private ArrayList<EditText> trackVal;

    ArrayList<Pair<String, Counter>> list = null;

    public Game(String in, MainActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();

        String[] temp = in.split(";");
        name = temp[0];
        for(int i=1;i<temp.length;i++) {
            handleValues(temp[i]);
        }
        if(temp.length<=1){
            trackers.add(new Pair<String, Counter>(null,null));
            //Log.d(TAG, "Empty tracker made");
        }
        Log.d(TAG, "Number of trackers made: "+trackers.size()); //debug
    }

    public String getName() {
        return name;
    }

    private void handleValues(String values){
        String[] temp = values.split("'");
        //Log.d(TAG, "handleValues, temp size: "+temp.length+": ["+temp[0]+"] "+temp[1]);
        String[] temp2 = temp[1].split("\"");
        //Log.d(TAG, "handleValues, temp2 size: "+temp2.length+": ["+temp2[0]+"] "+temp2[1]);
        try {
            Integer counterType = Integer.parseInt(temp2[0]);
            Counter counter = new Counter(counterType, temp2[1]);
            //Log.d(TAG, "Counter, intPrefs size: "+counter.getCounterValues().size());
            //Log.d(TAG, "Counter, intPrefs content: "+counter.getCounterValues().toString());
            trackers.add(new Pair<String, Counter>(temp[0], counter));
        }
        catch(Exception p){
            Log.e(TAG, "Parse exception at tracker creation.");
            Log.e(TAG, ""+p);
        }
    }

    public ArrayList<Pair<String,Counter>> getList() {
        return trackers;
    }

    public ArrayList<LinearLayout> getRows(){
        buildRows();
        return linList;
    }

    private void buildRows(){
        buttonsP = new ArrayList<>();
        buttonsM = new ArrayList<>();
        trackVal = new ArrayList<>();

        linList = new ArrayList<>();
        Log.d(TAG, "Creating trackers...");
        list = trackers;
        //Log.d(TAG, "Tracker list size: "+list.size()); //debug
        for (int cTrack = 0; cTrack<list.size();cTrack++) {
            try {

                lin = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker, null);

                TextView property_name = (TextView) lin.findViewById(R.id.property_name);
                property_name.setText(list.get(cTrack).first);

                Button btnMin = (Button) lin.findViewById(R.id.btnMin);
                buttonsM.add(btnMin);
                btnMin.setOnClickListener(new ButtonPressedListener());

                EditText property_value = (EditText) lin.findViewById(R.id.property_value);
                property_value.setText(list.get(cTrack).second.getDefault());
                trackVal.add(property_value);

                Button btnPl = (Button) lin.findViewById(R.id.btnPl);
                buttonsP.add(btnPl);
                btnPl.setOnClickListener(new ButtonPressedListener());

                linList.add(lin);
                //Log.d(TAG, "Tracker " + (cTrack+1) + " set up with size " + list.get(cTrack).second.getCounterValues().size()); //debug
            }
            catch(NullPointerException n){

            }
        }
        Log.d(TAG, "Trackers created");
    }

    public class ButtonPressedListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View v) {
            if(buttonsM.contains(v)) {
                int k = buttonsM.indexOf(v);
                try {
                    //String
                    if(list.get(k).second.getCounterType()==0) {
                        int j = list.get(k).second.setSelectedIndex(list.get(k).second.getSelectedIndex()-1);
                        trackVal.get(k).setText(list.get(k).second.getCounterValues().get(j).toString());
                    }
                    else //Integer
                    if(list.get(k).second.getCounterType()==1) {
                        int calc = Integer.parseInt(trackVal.get(k).getText().toString()) -1;
                        int min = (Integer) list.get(k).second.getCounterValues().get(1);
                        if(calc<min){
                            throw new Exception();
                        }
                        else trackVal.get(k).setText(String.valueOf(calc));
                    }
                } catch (IndexOutOfBoundsException n) {
                    Log.e(TAG, "Could not subtract from "+list.get(k).first+". "+n);
                } catch (Exception e){
                    Log.e(TAG, "Could not subtract from "+list.get(k).first+". "+e);
                }
            }
            else if(buttonsP.contains(v)){
                int k = buttonsP.indexOf(v);
                try{
                    //String
                    if(list.get(k).second.getCounterType()==0) {
                        int j = list.get(k).second.setSelectedIndex(list.get(k).second.getSelectedIndex()+1);
                        trackVal.get(k).setText(list.get(k).second.getCounterValues().get(j).toString());
                    }
                    else //Integer
                    if(list.get(k).second.getCounterType()==1) {
                        int calc = Integer.parseInt(trackVal.get(k).getText().toString()) +1;
                        int max = (Integer) list.get(k).second.getCounterValues().get(2);
                        if(calc>max){
                            throw new Exception();
                        }
                        else trackVal.get(k).setText(String.valueOf(calc));
                    }
                }
                catch(IndexOutOfBoundsException n){
                    Log.e(TAG, "Could not add to "+list.get(k).first+". "+n);
                } catch (Exception e){
                    Log.e(TAG, "Could not add to "+list.get(k).first+". "+e);
                }
            }
        }
    }
}
