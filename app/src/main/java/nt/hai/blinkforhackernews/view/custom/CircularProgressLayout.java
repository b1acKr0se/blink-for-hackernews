package nt.hai.blinkforhackernews.view.custom;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.FrameLayout;


@CoordinatorLayout.DefaultBehavior(CircularProgressLayoutBehavior.class)
public class CircularProgressLayout extends FrameLayout {

    public CircularProgressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
