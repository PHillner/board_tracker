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

    private ArrayList<Game> gamesList;
    private ArrayList<String> gameNames;
    private boolean hasRemovedFirst;

    public Games() {
        gamesList = new ArrayList<>();
        gameNames = new ArrayList<>();
        hasRemovedFirst = false;
    }

    public void addGames(ArrayList<Game> gamesList, ArrayList<String> gameNames) {
        this.gamesList = gamesList;
        this.gameNames = gameNames;
        //removeInit();
    }

    public ArrayList<Game> getGamesList(){
        return gamesList;
    }

    public ArrayList<String> getGameNames(){
        return gameNames;
    }

    private void removeInit(){
        if(hasRemovedFirst==false){
            gamesList.remove(0);
            gameNames.remove(0);
            hasRemovedFirst = true;
        }
    }
}
