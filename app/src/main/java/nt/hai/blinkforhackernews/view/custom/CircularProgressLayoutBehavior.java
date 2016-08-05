package nt.hai.blinkforhackernews.view.custom;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class CircularProgressLayoutBehavior extends CoordinatorLayout.Behavior<CircularProgressLayout> {
    public CircularProgressLayoutBehavior() {
        super();
    }

    public CircularProgressLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircularProgressLayout child,
                                   View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent,
                                          CircularProgressLayout child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        Log.i("TAG", "onDependentViewChanged: " + translationY);
        if (child.getBottom() > dependency.getTop()) {
            child.setTranslationY(translationY);

        }
        return true;
    }
}
