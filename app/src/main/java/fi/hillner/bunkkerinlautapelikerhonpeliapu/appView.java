package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Pat on 02.04.16.
 */
public class appView {

    private static appView instance;
    private static MainActivity activity;
    Games games;

    private LinearLayout linLayout;
    private static Context context;
    private Resources resources;
    private String output;

    private ArrayList<Game> gamesList = new ArrayList<>();
    private ArrayList<String> gameNames = new ArrayList<>();
    private ArrayList<Button> buttonsP = new ArrayList<>();
    private ArrayList<Button> buttonsM = new ArrayList<>();
    private ArrayList<TextView> trackVal = new ArrayList<>();
    ArrayList<Pair<String, CounterBuilder>> list = null;

    private appView() {
        //getGames();
    }

    private appView(MainActivity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public static appView getInstance() {
        return instance;
    }

    public static appView getInstance(MainActivity activ) {
        if(instance == null) {
            instance = new appView(activ);
        }
        else activity = activ;
        return instance;
    }

    public void buildRows(String game){
        linLayout = (LinearLayout) activity.findViewById(R.id.linLayout);
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
                //text.setTextColor(resources.getColor(R.color.black)); not working :<
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
                //value.setTextColor(resources.getColor(R.color.colorPrimaryDark)); not working :<
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
        }
    }
}
