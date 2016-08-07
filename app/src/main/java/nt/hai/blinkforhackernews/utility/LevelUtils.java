package nt.hai.blinkforhackernews.utility;

import java.util.HashMap;

import nt.hai.blinkforhackernews.R;

/**
 * Created by FRAMGIA\nguyen.thanh.hai on 08/08/2016.
 */

public class LevelUtils {
    private static LevelUtils instance;
    private static HashMap<Integer, Integer> levelIndicator;

    private LevelUtils() {

    }

    public static LevelUtils getInstance() {
        if (instance == null) {
            instance = new LevelUtils();
            levelIndicator = new HashMap<>();
            levelIndicator.put(0, R.color.level_0_pink);
            levelIndicator.put(1, R.color.level_1_indigo);
            levelIndicator.put(2, R.color.level_2_red);
            levelIndicator.put(3, R.color.level_3_green);
            levelIndicator.put(4, R.color.level_4_orange);
            levelIndicator.put(5, R.color.level_5_purple);
            levelIndicator.put(6, R.color.level_6_cyan);
            levelIndicator.put(7, R.color.level_7_orange);
            levelIndicator.put(8, R.color.level_8_teal);
        }
        return instance;
    }

    public int getColor(int level) {
        return levelIndicator.get(level);
    }
}
