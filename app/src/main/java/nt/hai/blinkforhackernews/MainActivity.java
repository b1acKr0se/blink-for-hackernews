package nt.hai.blinkforhackernews;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import nt.hai.blinkforhackernews.utility.HardwareUtils;
import nt.hai.blinkforhackernews.view.OnTitleClickListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private OnTitleClickListener onTitleClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("pref_dark_theme", false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView appNameTextView = (TextView) findViewById(R.id.app_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "azurite.ttf");
        appNameTextView.setTypeface(typeface);
        appNameTextView.setText(getString(R.string.app_name));
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
        if (HardwareUtils.hasSoftKeys(getWindowManager())) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("pref_has_softkey", true).apply();
        } else {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("pref_has_softkey", false).apply();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.coordinator_layout).setPadding(0, getResources().getDimensionPixelSize(R.dimen.coordinator_layout_padding_top), 0, 0);
            findViewById(R.id.toolbar_container).setPadding(0, getResources().getDimensionPixelSize(R.dimen.toolbar_container_padding_top), 0, 0);
        } else {
            findViewById(R.id.coordinator_layout).setPadding(0, 0, 0, 0);
            findViewById(R.id.toolbar_container).setPadding(0, 0, 0, 0);
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_theme:
                changeTheme();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeTheme() {
        boolean darkTheme = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("pref_dark_theme", false);
        if (darkTheme) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("pref_dark_theme", false).apply();
        } else {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("pref_dark_theme", true).apply();
        }
        finish();
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.app_title) {
            if (onTitleClickListener != null) onTitleClickListener.onTitleClick();
        }
    }
}
