package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import java.util.LinkedList;

/**
 * Created by Pat on 11.01.16.
 */
public class CounterBuilder {
    private String def;
    private LinkedList<String> nodes = new LinkedList<>();
    private LinkedList<Integer> intPrefs = new LinkedList<>();  //[default, min, max]
    private int counterType;

    public CounterBuilder(Integer counterType, String nodesGet){
        this.counterType = counterType;
        if(counterType==0) {
            String[] vals = nodesGet.split(",");
            def = vals[0];
            for (int i = 1; i < vals.length; i++) {
                nodes.add(vals[i]);
            }
        }
        if(counterType==1){
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
}
