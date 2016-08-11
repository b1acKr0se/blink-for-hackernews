package nt.hai.blinkforhackernews.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.utility.HardwareUtils;
import nt.hai.blinkforhackernews.view.custom.NestedWebView;

public class WebFragment extends Fragment implements OnBackActionListener {
    private NestedWebView webView;
    private OnUrlLoadingListener listener;
    private OnTitleChangeListener onTitleChangeListener;
    private OnBackActionCallback onBackActionCallback;
    private String url;

    public WebFragment() {
        // Required empty public constructor
    }

    static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    void setOnUrlLoadingListener(OnUrlLoadingListener listener) {
        this.listener = listener;
    }

    void setOnTitleChangeListener(OnTitleChangeListener listener) {
        this.onTitleChangeListener = listener;
    }

    void setOnBackActionCallback(OnBackActionCallback callback) {
        this.onBackActionCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        webView = (NestedWebView) view.findViewById(R.id.nested_web_view);
        url = getArguments().getString("url");
        changeTitle(null, url);
        setUpPadding();
        setupWebView();
        return view;
    }

    private void changeTitle(String title, String subtitle) {
        if (onTitleChangeListener == null)
            return;
        onTitleChangeListener.onTitleChange(title, subtitle);
    }

    private void setUpPadding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (HardwareUtils.hasSoftKeys(getActivity().getWindowManager())) {
                webView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.recycler_padding_bottom));
            } else {
                webView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.normal_padding_bottom));
            }
        }
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebChromeClient(new HNWebChromeClient(listener));
        webView.setWebViewClient(new HNWebViewClient(onTitleChangeListener));
        webView.loadUrl(url);
    }

    @Override
    public void onBack() {
        WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
        if (mWebBackForwardList.getCurrentIndex() > 0)
            webView.loadUrl(mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl());
        else
            onBackActionCallback.onCallback();
    }
}
