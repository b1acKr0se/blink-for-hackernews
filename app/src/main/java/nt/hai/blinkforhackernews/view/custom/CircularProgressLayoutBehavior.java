package nt.hai.blinkforhackernews.view.custom;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import nt.hai.blinkforhackernews.utility.DimenUtils;


public class CircularProgressLayoutBehavior extends CoordinatorLayout.Behavior<CircularProgressLayout> {
    private int toolbarHeight;
    private Context context;
    private boolean hasSoftkey;

    public CircularProgressLayoutBehavior() {
        super();
    }

    public CircularProgressLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.toolbarHeight = DimenUtils.getToolbarHeight(context);
        this.hasSoftkey = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("pref_has_softkey", false);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircularProgressLayout child,
                                   View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent,
                                          CircularProgressLayout child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            int distanceToScroll = child.getHeight();
            float ratio = dependency.getY() / (float) toolbarHeight;
            float translation;
            if (hasSoftkey) {
                translation = -distanceToScroll * (ratio + 0.38f);
            } else {
                translation = -distanceToScroll * (ratio + 0.4f);
            }
            child.setTranslationY(translation);
        }
        return true;
    }
}
