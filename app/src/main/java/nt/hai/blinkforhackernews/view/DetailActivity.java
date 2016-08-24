package nt.hai.blinkforhackernews.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.view.chromecustomtab.CustomTabActivityHelper;

public class DetailActivity extends AnimBaseActivity implements OnUrlLoadingListener, View.OnClickListener, OnTitleChangeListener, OnBackActionCallback {
    private FloatingActionButton floatingActionButton;
    private WebFragment webFragment;
    private CommentFragment commentFragment;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private OnBackActionListener onBackActionListener;
    private Item item;

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
        ActivityCompat.postponeEnterTransition(this);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        findViewById(R.id.container).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(this);
        setUpPadding();
        readIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private void readIntent() {
        Intent intent = getIntent();
        item = (Item) intent.getExtras().getSerializable("item");
//        createWebFragment();
        createCommentFragment();
        navigateToComment();
    }

    private void createWebFragment() {
        webFragment = WebFragment.newInstance(item.getUrl());
        webFragment.setOnUrlLoadingListener(this);
        webFragment.setOnTitleChangeListener(this);
        webFragment.setOnBackActionCallback(this);
        setOnBackActionListener(webFragment);
    }

    private void createCommentFragment() {
        commentFragment = CommentFragment.newInstance(item);
    }

    void navigateToWeb() {
        findViewById(R.id.container).setVisibility(View.VISIBLE);
        appBarLayout.setExpanded(true, true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, 0, 0, 0);
        if (webFragment != null) {
            if (webFragment.isAdded()) fragmentTransaction.show(webFragment);
            else
                fragmentTransaction.add(R.id.container, webFragment, "Web");
        } else {
            createWebFragment();
            fragmentTransaction.add(R.id.container, webFragment, "Web");
        }
        if (commentFragment != null)
            if (commentFragment.isAdded()) fragmentTransaction.hide(commentFragment);
        fragmentTransaction.commit();
    }

    void navigateToComment() {
        findViewById(R.id.container).setVisibility(View.VISIBLE);
        appBarLayout.setExpanded(true, true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, 0, 0, 0);
        if (commentFragment != null) {
            if (commentFragment.isAdded()) fragmentTransaction.show(commentFragment);
            else
                fragmentTransaction.add(R.id.container, commentFragment, "Comment");
        } else {
            createCommentFragment();
            fragmentTransaction.add(R.id.container, commentFragment, "Comment");
        }
        if (webFragment != null)
            if (webFragment.isAdded()) fragmentTransaction.hide(webFragment);
        fragmentTransaction.commit();
    }

    void setOnBackActionListener(OnBackActionListener listener) {
        this.onBackActionListener = listener;
    }

    @Override
    public void onProgressChanged(int progress) {
//        progressBar.setVisibility(progress == 100 ? View.INVISIBLE : View.VISIBLE);
//        progressBar.setProgress(progress);
    }

    @Override
    public void onClick(View view) {
        Uri uri = Uri.parse(item.getUrl());
        switch (view.getId()) {
            case R.id.floating_action_button:
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary)).setStartAnimations(this, 0, 0).setExitAnimations(this, 0, R.anim.fade_out).build();
                CustomTabActivityHelper.openCustomTab(this, customTabsIntent, uri,
                        new CustomTabActivityHelper.CustomTabFallback() {
                            @Override
                            public void openUri(Activity activity, Uri uri) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                activity.startActivity(intent);
                            }
                        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCallback() {
        finish();
    }
}
