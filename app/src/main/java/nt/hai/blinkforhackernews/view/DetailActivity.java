package nt.hai.blinkforhackernews.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.utility.HardwareUtils;

public class DetailActivity extends AnimBaseActivity implements OnUrlLoadingListener, View.OnClickListener {
    private View buttonContainer;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton floatingActionButton;
    private WebFragment webFragment;
    private CommentFragment commentFragment;


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
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        buttonContainer = findViewById(R.id.button_container);
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
        commentFragment = CommentFragment.newInstance(item.getTitle());
        navigateToWeb();
    }

    void navigateToWeb() {
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

    @Override
    public void onProgressChanged(int progress) {
        if (progress == 100)
            progressBar.setVisibility(View.GONE);
        progressBar.setProgress(progress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_action_button:
                if(webFragment != null && !webFragment.isHidden()) {
                    navigateToComment();
                } else if (webFragment != null && webFragment.isHidden()) {
                    navigateToWeb();
                }
                break;

        }
    }
}
