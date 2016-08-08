package nt.hai.blinkforhackernews.utility;

import android.content.Context;
import android.content.res.TypedArray;

import nt.hai.blinkforhackernews.R;

public class ColorCode {
    private static ColorCode instance;
    private static TypedArray levelIndicator;

    private ColorCode() {

    }

    public static ColorCode getInstance(Context context) {
        if (instance == null) {
            instance = new ColorCode();
            levelIndicator = context.getResources().obtainTypedArray(R.array.color_codes);
        }
        return instance;
    }

    public int getColor(int level) {
        return levelIndicator.getColor(level, 0);
    }
}
