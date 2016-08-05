package nt.hai.blinkforhackernews.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import nt.hai.blinkforhackernews.swipelayout.SwipeBackLayout;
import nt.hai.blinkforhackernews.swipelayout.Utils;
import nt.hai.blinkforhackernews.swipelayout.app.SwipeBackActivityBase;
import nt.hai.blinkforhackernews.swipelayout.app.SwipeBackActivityHelper;


public class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new SwipeBackActivityHelper(this);
        helper.onActivityCreate();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        helper.getSwipeBackLayout().mDragHelper.override = true;
        helper.getSwipeBackLayout().setEdgeSize(metrics.widthPixels);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        helper.onPostCreate();
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return helper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
