package nt.hai.blinkforhackernews.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.utility.ColorCode;
import nt.hai.blinkforhackernews.utility.DateUtils;
import nt.hai.blinkforhackernews.utility.LinkUtils;

public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
    public TextView author, time, content, collapseNumber;
    public View level;
    public View container;
    private Context context;
    private OnCommentClickListener onCommentClickListener;
    private Item item;
    public LinearLayout menuArea;
    private static final long CLICK_TIME_INTERVAL = 500;
    private long lastClickTime = System.currentTimeMillis();


    public CommentViewHolder(View view) {
        super(view);
        context = itemView.getContext();
        author = (TextView) view.findViewById(R.id.comment_author);
        time = (TextView) view.findViewById(R.id.comment_time);
        content = (TextView) view.findViewById(R.id.comment_content);
        level = view.findViewById(R.id.comment_level);
        collapseNumber = (TextView) view.findViewById(R.id.comment_collapse_item);
        menuArea = (LinearLayout) view.findViewById(R.id.menuarea);
        container = view.findViewById(R.id.container);
    }

    public void bind(Item item, OnCommentClickListener listener, int collapse) {
        this.onCommentClickListener = listener;
        this.item = item;
        if (item.getLevel() > 0) {
            level.setVisibility(View.VISIBLE);
            level.setBackgroundColor(ColorCode.getInstance(context).getColor(item.getLevel()));
        } else
            level.setVisibility(View.GONE);
        if (!item.isLoaded()) {
            author.setText("...");
            content.setText("...");
            time.setText("");
            collapseNumber.setVisibility(View.GONE);
            return;
        }
        if (item.isDeleted()) {
            markDeleted(time, DateUtils.getReadableDate(item.getTime()));
            content.setText("");
            author.setText("");
            return;
        }
        itemView.setOnClickListener(this);
        author.setText(item.getBy());
        time.setText(DateUtils.getReadableDate(item.getTime()));
        LinkUtils.setTextWithLinks(content, LinkUtils.fromHtml(item.getText(), false));
        content.setOnClickListener(this);
        if (!item.isExpanded() && item.getKids() != null) {
            collapseNumber.setVisibility(View.VISIBLE);
            collapseNumber.setText("+" + String.valueOf(collapse));
        } else {
            collapseNumber.setVisibility(View.GONE);
        }
    }

    private void markDeleted(TextView timeTextView, String text) {
        timeTextView.setText(text, TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) timeTextView.getText();
        spannable.setSpan(STRIKE_THROUGH_SPAN, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    @Override
    public void onClick(View view) {
        long now = System.currentTimeMillis();
        if (now - lastClickTime < CLICK_TIME_INTERVAL) {
            return;
        }
        lastClickTime = now;
        switch (view.getId()) {
            case R.id.comment_content:
                if (content.getSelectionStart() < 0 && content.getSelectionEnd() < 0) {
                    if (onCommentClickListener != null) onCommentClickListener.onCommentClick(item);
                }
                break;
            default:
                if (onCommentClickListener != null) onCommentClickListener.onCommentClick(item);
        }
    }
}