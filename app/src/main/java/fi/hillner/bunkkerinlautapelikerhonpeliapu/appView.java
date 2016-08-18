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

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Pat on 02.04.16.
 */
public class appView {

    private static appView instance;
    private static MainActivity activity;

    private appView() {
    }

    private appView(MainActivity activity){
        this.activity = activity;
        //resources = this.activity.getResources();
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

    public ArrayList<LinearLayout> getRows(Game game){
        return game.getRows();
    }
}
