package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by Pat on 11.01.16.
 */
public class Counter {
    private String def;
    private LinkedList<String> nodes = new LinkedList<>();      //{default, 1, 2, ..., n-2, n-1} (n = vals length)
    private LinkedList<Integer> intPrefs = new LinkedList<>();  //[default, min, max]
    private int counterType;
    private int selectedIndex;

    private static final String TAG = "Counter";

    public Counter(Integer counterType, String nodesGet){
        this.counterType = counterType;
        //String
        if(counterType==0) {
            // default, 1, 2, ..., n-2, n-1 (n = vals length)
            String[] vals = nodesGet.split(",");
            def = vals[0];
            for (int i = 1; i < vals.length; i++) {
                nodes.add(vals[i]);
                if(vals[i].equals(def)) selectedIndex = i-1;
            }
        }else
        //Integer
        if(counterType==1){
            // default, min, max
            String[] temp = nodesGet.split(",");
            def = temp[0];
            for(int i=0;i<temp.length;i++) {
                intPrefs.add(Integer.parseInt(temp[i]));
            }
        }
    }

    @Override
    public String toString(){
        String info = intPrefs.get(0)+", "+intPrefs.get(1)+", "+intPrefs.get(2);
        return info;
    }

    public int getCounterType(){
        return counterType;
    }

    public LinkedList<?> getCounterValues(){
        if(counterType==0)
            return nodes;
        else if(counterType==1)
            return intPrefs;
        return null;
    }

    public String getDefault(){ return def; }

    public void setDefault(String def){
        this.def = def;
        intPrefs.set(0,Integer.parseInt(def));
    }

    public int setSelectedIndex(int i){
        if(0<=i && i<nodes.size()){ // (min < i && i < max)
            selectedIndex = i;
        }
        Log.d(TAG, "setSelectedIndex: "+selectedIndex);       //debug
        return selectedIndex;
    }

    public int getSelectedIndex(){
        return selectedIndex;
    }
}
