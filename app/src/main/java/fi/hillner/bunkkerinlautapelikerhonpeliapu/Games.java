package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pat on 02.04.16.
 */
public class Games {

    private static Games instance;

    private ArrayList<Game> gamesList = new ArrayList<>();
    private ArrayList<String> gameNames = new ArrayList<>();
    private boolean hasRemovedFirst = false;

    private Games() {

    }

    public static Games getInstance() {
        if(instance == null) {
            instance = new Games();
        }
        return instance;
    }

    public void addGames(ArrayList<Game> gamesList, ArrayList<String> gameNames) {
        this.gamesList = gamesList;
        this.gameNames = gameNames;
    }

    public ArrayList<Game> getGamesList(){
        return gamesList;
    }

    public ArrayList<String> getGameNames(){
        return gameNames;
    }

    public void removeInit(){
        if(hasRemovedFirst==false){
            gamesList.remove(0);
            gameNames.remove(0);
            hasRemovedFirst = true;
        }
    }
}
