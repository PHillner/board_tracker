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
            //System.out.println("Empty tracker made");
        }
        System.out.println("num trackers made: "+trackers.size()); //debug
    }

    public String getName() {
        return name;
    }

    private void handleValues(String values){
        String[] temp = values.split("'");
        //System.out.println("handleValues, temp size: "+temp.length+": ["+temp[0]+"] "+temp[1]);
        String[] temp2 = temp[1].split("\"");
        //System.out.println("handleValues, temp2 size: "+temp2.length+": ["+temp2[0]+"] "+temp2[1]);
        try {
            Integer counterType = Integer.parseInt(temp2[0]);
            Counter counter = new Counter(counterType, temp2[1]);
            //System.out.println("Counter, intPrefs size: "+counter.getCounterValues().size());
            //System.out.println("Counter, intPrefs content: "+counter.getCounterValues().toString());
            trackers.add(new Pair<String, Counter>(temp[0], counter));
        }
        catch(Exception p){
            System.out.println("Parse exception at tracker creation.");
            System.err.println(p);
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
        System.out.println("Creating trackers...");
        list = trackers;
        //System.out.println("Tracker list size: "+list.size()); //debug
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
                //System.out.println("Tracker " + (cTrack+1) + " set up with size " + list.get(cTrack).second.getCounterValues().size()); //debug
            }
            catch(NullPointerException n){

            }
        }
        System.out.println("Trackers created");
    }

    public class ButtonPressedListener implements AdapterView.OnClickListener {

        @Override
        public void onClick(View v) {
            if(buttonsM.contains(v)) {
                int k = buttonsM.indexOf(v);
                System.out.println("button row index: "+k);
                System.out.println("selected value index: "+list.get(k).second.getSelectedIndex());
                System.out.println("counter type: "+list.get(k).second.getCounterType());
                System.out.println("list size: "+list.size());
                System.out.println("counterValue size: "+list.get(k).second.getCounterValues().size());
                try {
                    //String
                    if(list.get(k).second.getCounterType()==0) {
                        int j = list.get(k).second.setSelectedIndex(list.get(k).second.getSelectedIndex()-1);
                        System.out.println("new selected index: "+j);
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
                    Log.i("err", "Could not subtract from "+list.get(k).first+". "+n);
                } catch (Exception e){
                    Log.i("err", "Could not subtract from "+list.get(k).first+". "+e);
                }
            }
            else if(buttonsP.contains(v)){
                int k = buttonsP.indexOf(v);
                System.out.println("button row index: "+k);
                System.out.println("selected value index: "+list.get(k).second.getSelectedIndex());
                System.out.println("counter type: "+list.get(k).second.getCounterType());
                System.out.println("list size: "+list.size());
                System.out.println("counterValue size: "+list.get(k).second.getCounterValues().size());
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
                    Log.i("err", "Could not add to "+list.get(k).first+". "+n);
                } catch (Exception e){
                    Log.i("err", "Could not add to "+list.get(k).first+". "+e);
                }
            }
        }
    }
}
