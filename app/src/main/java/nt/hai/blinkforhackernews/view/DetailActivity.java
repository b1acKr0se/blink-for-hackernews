package nt.hai.blinkforhackernews.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.utility.HardwareUtils;

public class DetailActivity extends AnimBaseActivity implements OnUrlLoadingListener, View.OnClickListener, OnTitleChangeListener, OnBackActionCallback {
    private View buttonContainer;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private WebFragment webFragment;
    private CommentFragment commentFragment;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private OnBackActionListener onBackActionListener;

    public static void navigate(Context context, Item item) {
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        buttonContainer = findViewById(R.id.button_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(this);
        setUpPadding();
        readIntent();
    }

    private void setUpPadding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.coordinator_layout).setPadding(0, getResources().getDimensionPixelSize(R.dimen.coordinator_layout_padding_top), 0, 0);
            findViewById(R.id.toolbar_container).setPadding(0, getResources().getDimensionPixelSize(R.dimen.toolbar_container_padding_top), 0, 0);
            if (HardwareUtils.hasSoftKeys(getWindowManager())) {
                buttonContainer.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.recycler_padding_bottom));
            }
        } else {
            findViewById(R.id.coordinator_layout).setPadding(0, 0, 0, 0);
            findViewById(R.id.toolbar_container).setPadding(0, 0, 0, 0);
        }
    }

    private void readIntent() {
        Intent intent = getIntent();
        Item item = (Item) intent.getExtras().getSerializable("item");
        webFragment = WebFragment.newInstance(item.getUrl());
        webFragment.setOnUrlLoadingListener(this);
        webFragment.setOnTitleChangeListener(this);
        webFragment.setOnBackActionCallback(this);
        setOnBackActionListener(webFragment);
        commentFragment = CommentFragment.newInstance(item);
        navigateToWeb();
    }

    void navigateToWeb() {
        appBarLayout.setExpanded(true, true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (webFragment != null && webFragment.isAdded())
            fragmentTransaction.show(webFragment);
        else
            fragmentTransaction.add(R.id.container, webFragment, "Web");
        if (commentFragment != null && commentFragment.isAdded())
            fragmentTransaction.hide(commentFragment);
        fragmentTransaction.commit();
    }

    void navigateToComment() {
        appBarLayout.setExpanded(true, true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (commentFragment != null && commentFragment.isAdded())
            fragmentTransaction.show(commentFragment);
        else
            fragmentTransaction.add(R.id.container, commentFragment, "Comment");
        if (webFragment != null && webFragment.isAdded())
            fragmentTransaction.hide(webFragment);
        fragmentTransaction.commit();
    }

    void setOnBackActionListener(OnBackActionListener listener) {
        this.onBackActionListener = listener;
    }

    @Override
    public void onProgressChanged(int progress) {
        progressBar.setVisibility(progress == 100 ? View.INVISIBLE : View.VISIBLE);
        progressBar.setProgress(progress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_action_button:
                if (webFragment != null && !webFragment.isHidden()) {
                    navigateToComment();
                } else if (webFragment != null && webFragment.isHidden()) {
                    navigateToWeb();
                }
                break;

        }
    }

    @Override
    public void onTitleChange(String title, String subtitle) {
        if (getSupportActionBar() == null)
            return;
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackActionListener.onBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onCallback() {
        finish();
    }
}
