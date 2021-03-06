package nt.hai.blinkforhackernews.view.custom;


import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
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
    private int titleColor;
    private int linkColor;
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
        if (TextUtils.isEmpty(link)) this.link = "";
        else this.link = "(" + link + ")";
        return this;
    }

    private void init() {
        titleTextSize = getResources().getDimensionPixelSize(R.dimen.title_text_size);
        linkTextSize = getResources().getDimensionPixelSize(R.dimen.link_text_size);
        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("pref_dark_theme", false)) {
            titleColor = R.color.textColorPrimary;
            linkColor = R.color.textColorSecondaryInverse;
            return;
        }
        titleColor = R.color.textColorPrimaryInverse;
        linkColor = R.color.textColorSecondary;
    }

    public void draw() {
        if (title == null) {
            setText("");
            return;
        }
        SpannableString span1 = new SpannableString(title);
        span1.setSpan(new AbsoluteSizeSpan(titleTextSize), 0, title.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        span1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), titleColor)), 0, title.length(), 0);
        SpannableString span2 = new SpannableString(link);
        span2.setSpan(new AbsoluteSizeSpan(linkTextSize), 0, link.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        span2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), linkColor)), 0, link.length(), 0);
        CharSequence finalText = TextUtils.concat(span1, " ", span2);
        setText(finalText);
    }
}
