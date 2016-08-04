package nt.hai.blinkforhackernews.view.custom;


import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import nt.hai.blinkforhackernews.R;

public class MultiSizeTextView extends TextView {
    private int titleTextSize = -1;
    private int linkTextSize = -1;
    private String title;
    private String link;

    public MultiSizeTextView(Context context) {
        super(context);
        init();
    }

    public MultiSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiSizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MultiSizeTextView setTitleTextSize(int sizeInPixel) {
        this.titleTextSize = sizeInPixel;
        return this;
    }

    public MultiSizeTextView setLinkTextSize(int sizeInPixel) {
        this.linkTextSize = sizeInPixel;
        return this;
    }

    public MultiSizeTextView setTitle(String title) {
        this.title = title;
        return this;
    }

    public MultiSizeTextView setLink(String link) {
        this.link = "(" + link + ")";
        return this;
    }

    private void init() {
        titleTextSize = getResources().getDimensionPixelSize(R.dimen.title_text_size);
        linkTextSize = getResources().getDimensionPixelSize(R.dimen.link_text_size);
    }

    public void draw() {
        if (title == null) {
            setText("---------------");
            return;
        }
        SpannableString span1 = new SpannableString(title);
        span1.setSpan(new AbsoluteSizeSpan(titleTextSize), 0, title.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        span1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), 0);
        SpannableString span2 = new SpannableString(link);
        span2.setSpan(new AbsoluteSizeSpan(linkTextSize), 0, link.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        CharSequence finalText = TextUtils.concat(span1, " ", span2);
        setText(finalText);
    }
}
