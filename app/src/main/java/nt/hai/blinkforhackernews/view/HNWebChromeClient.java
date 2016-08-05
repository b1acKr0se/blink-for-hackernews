package nt.hai.blinkforhackernews.view;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class HNWebChromeClient extends WebChromeClient {
    private OnUrlLoadingListener listener;

    public HNWebChromeClient(OnUrlLoadingListener listener) {
        this.listener = listener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (listener != null) listener.onProgressChanged(newProgress);
    }
}
