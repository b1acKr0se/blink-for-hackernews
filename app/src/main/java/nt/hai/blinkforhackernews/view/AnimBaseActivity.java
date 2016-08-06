package nt.hai.blinkforhackernews.view;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.swipelayout.app.SwipeBackActivityBase;

public class AnimBaseActivity extends BaseActivity implements SwipeBackActivityBase {

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.fade_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("pref_dark_theme", false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_right, 0);

    }
}
