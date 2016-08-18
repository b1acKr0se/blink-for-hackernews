package nt.hai.blinkforhackernews.view;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import nt.hai.blinkforhackernews.utility.LinkUtils;


public class HNWebViewClient extends WebViewClient {
    private OnTitleChangeListener onTitleChangeListener;

    public HNWebViewClient(OnTitleChangeListener listener) {
        this.onTitleChangeListener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (onTitleChangeListener == null)
            return;
        onTitleChangeListener.onTitleChange(view.getTitle(), LinkUtils.getHostName(url));
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (onTitleChangeListener == null)
            return;
        onTitleChangeListener.onTitleChange(null, url);
    }
}
