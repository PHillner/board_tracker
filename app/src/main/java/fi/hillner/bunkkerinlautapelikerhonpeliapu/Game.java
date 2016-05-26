package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Pat on 11.01.16.
 */
public class Game{
    private String name;
    private ArrayList<Pair<String,CounterBuilder>> trackers = new ArrayList<>();

    public Game(String in) {
        String[] temp = in.split(";");
        name = temp[0];
        for(int i=1;i<temp.length;i++)
            handleValues(temp[i]);
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
}
