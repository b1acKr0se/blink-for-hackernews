package nt.hai.blinkforhackernews;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import nt.hai.blinkforhackernews.utility.HardwareUtils;
import nt.hai.blinkforhackernews.view.OnTitleClickListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private OnTitleClickListener onTitleClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView appNameTextView = (TextView) findViewById(R.id.app_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "azurite.ttf");
        appNameTextView.setTypeface(typeface);
        appNameTextView.setOnClickListener(this);
        setUpPadding();
        showListFragment();
    }

    private void showListFragment() {
        ItemListFragment fragment = ItemListFragment.newInstance();
        onTitleClickListener = fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    private void setUpPadding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.coordinator_layout).setPadding(0, getResources().getDimensionPixelSize(R.dimen.coordinator_layout_padding_top), 0, 0);
            findViewById(R.id.toolbar_container).setPadding(0, getResources().getDimensionPixelSize(R.dimen.toolbar_container_padding_top), 0, 0);
        } else {
            findViewById(R.id.coordinator_layout).setPadding(0, 0, 0, 0);
            findViewById(R.id.toolbar_container).setPadding(0, 0, 0, 0);
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.app_title) {
            if (onTitleClickListener != null) onTitleClickListener.onTitleClick();
        }
    }
}
