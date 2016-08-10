package nt.hai.blinkforhackernews.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.StrikethroughSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.utility.ColorCode;
import nt.hai.blinkforhackernews.utility.DateUtils;

public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
    private TextView author, time, content;
    private View level;
    private Context context;
    private OnCommentClickListener onCommentClickListener;
    private Item item;


    public CommentViewHolder(View view) {
        super(view);
        context = itemView.getContext();
        author = (TextView) view.findViewById(R.id.comment_author);
        time = (TextView) view.findViewById(R.id.comment_time);
        content = (TextView) view.findViewById(R.id.comment_content);
        level = view.findViewById(R.id.comment_level);
    }

    public void bind(Item item, OnCommentClickListener listener) {
        this.onCommentClickListener = listener;
        this.item = item;
        level.setBackgroundColor(ColorCode.getInstance(context).getColor(item.getLevel()));
        if (!item.isLoaded()) {
            author.setText("...");
            content.setText("...");
            time.setText("");
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
        content.setText(Html.fromHtml(item.getText() == null ? "" : item.getText()));
        Linkify.addLinks(content, Linkify.ALL);
    }

    private void markDeleted(TextView timeTextView, String text) {
        timeTextView.setText(text, TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) timeTextView.getText();
        spannable.setSpan(STRIKE_THROUGH_SPAN, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void onClick(View view) {
        if (onCommentClickListener != null) onCommentClickListener.onCommentClick(item);
    }
}
