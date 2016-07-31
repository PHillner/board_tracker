package fi.hillner.bunkkerinlautapelikerhonpeliapu;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Pat on 31.07.16.
 */
public class RetainedFragment extends Fragment {
    // data object we want to retain
    private String game;
    private String[] values;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = "";
        values = null;
        // retain this fragment
        setRetainInstance(true);
    }

    /**
     *
     * @param game The name of the game
     * @param values The values of the spinners
     */
    public void setData(String game, String[] values) {
        this.game = game;
        this.values = values;
    }

    /**
     *
     * @return [gameName, value1, value2, ...]
     */
    public String[] getData() {
        String[] data = new String[(values.length +1)];
        data[0] = game;
        for(int i = 0; i<data.length; i++){
            data[i+1] = values[i];
        }
        return data;
    }

}
