package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
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

    //ArrayList<Pair<String label, CounterBuilder values>>
    private ArrayList<Pair<String,CounterBuilder>> trackers = new ArrayList<>();

    private static MainActivity activity;
    private static Context context;

    private ArrayList<LinearLayout> linList;
    private LinearLayout lin;
    private ArrayList<Button> buttonsP;
    private ArrayList<Button> buttonsM;
    private ArrayList<TextView> trackVal;

    ArrayList<Pair<String, CounterBuilder>> list = null;

    public Game(String in, MainActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();

        String[] temp = in.split(";");
        name = temp[0];
        for(int i=1;i<temp.length;i++) {
            handleValues(temp[i]);
        }
        if(temp.length<=1){
            trackers.add(new Pair<String, CounterBuilder>(null,null));
            //System.out.println("Empty tracker made");
        }
        //System.out.println("num trackers made: "+trackers.size()); //debug
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
            CounterBuilder counter = new CounterBuilder(counterType, temp2[1]);
            //System.out.println("CounterBuilder, intPrefs size: "+counter.getCounterValues().size());
            //System.out.println("CounterBuilder, intPrefs content: "+counter.getCounterValues().toString());
            trackers.add(new Pair<String, CounterBuilder>(temp[0], counter));
        }
        catch(Exception p){
            System.out.println("Parse exception at tracker creation.");
        }
    }

    public ArrayList<Pair<String,CounterBuilder>> getList() {
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
                lin = new LinearLayout(context);
                lin.setOrientation(LinearLayout.HORIZONTAL);
                //System.out.println("Setting tracker " + (cTrack+1)); //debug

                TextView property_name = new TextView(context);
                property_name.setText(list.get(cTrack).first);
                property_name.setTextColor(0xcc000000);
                property_name.setTextSize(18);
                property_name.setWidth(250);
                property_name.setPadding(25, 0, 0, 0);
                lin.addView(property_name);

                Button btnMin = new Button(context);
                btnMin.setText("-");
                btnMin.setTextSize(18);
                btnMin.setMaxWidth(50);
                btnMin.setOnClickListener(new ButtonPressedListener());
                buttonsM.add(btnMin);
                lin.addView(btnMin);

                TextView property_value = new TextView(context);
                property_value.setText(list.get(cTrack).second.getDefault());
                property_value.setTextColor(0xcc000000);
                property_value.setTextSize(18);
                property_value.setWidth(200);
                property_value.setPadding(25, 0, 0, 0);
                trackVal.add(property_value);
                lin.addView(property_value);

                Button btnPl = new Button(context);
                btnPl.setText("+");
                btnPl.setTextSize(18);
                btnPl.setMaxWidth(50);
                btnPl.setOnClickListener(new ButtonPressedListener());
                buttonsP.add(btnPl);
                lin.addView(btnPl);

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
                for(int g=0;g<buttonsM.size();g++){
                }
                try {
                    //String
                    if(list.get(k).second.getCounterType()==0) {
                        int j = list.get(k).second.getCounterValues().indexOf(trackVal.get(k).getText());
                        trackVal.get(k).setText(list.get(k).second.getCounterValues().get(j-1).toString());
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
                    Log.i("test", "Could not subtract from "+list.get(k).first+". "+n);
                } catch (Exception e){
                    Log.i("test", "Could not subtract from "+list.get(k).first+". "+e);
                }
            }
            else if(buttonsP.contains(v)){
                int k = buttonsP.indexOf(v);
                try{
                    //String
                    if(list.get(k).second.getCounterType()==0) {
                        int j = list.get(k).second.getCounterValues().indexOf(trackVal.get(k).getText());
                        trackVal.get(k).setText(list.get(k).second.getCounterValues().get(j+1).toString());
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
                    Log.i("test", "Could not add to "+list.get(k).first+". "+n);
                } catch (Exception e){
                    Log.i("test", "Could not add to "+list.get(k).first+". "+e);
                }
            }
        }
    }
}
