package nt.hai.blinkforhackernews.view;

import android.webkit.WebView;
import android.webkit.WebViewClient;


public class HNWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
