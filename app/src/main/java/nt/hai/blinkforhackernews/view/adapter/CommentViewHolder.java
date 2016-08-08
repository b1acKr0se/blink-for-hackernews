package nt.hai.blinkforhackernews.view.adapter;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.utility.DateUtils;
import nt.hai.blinkforhackernews.utility.LevelUtils;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    private TextView author, score, time, content;
    private View level;

    public CommentViewHolder(View view) {
        super(view);
        author = (TextView) view.findViewById(R.id.comment_author);
        score = (TextView) view.findViewById(R.id.comment_score);
        time = (TextView) view.findViewById(R.id.comment_time);
        content = (TextView) view.findViewById(R.id.comment_content);
        level = view.findViewById(R.id.comment_level);
    }

    public void bind(Item item) {
        level.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), LevelUtils.getInstance().getColor(item.getLevel())));
        if (!item.isLoaded()) {
            author.setText("...");
            content.setText("...");
            time.setText("");
            score.setText("");
            return;
        }
        author.setText(item.getBy());
        score.setText(String.valueOf(item.getScore()) + " pts");
        time.setText(DateUtils.getReadableDate(item.getTime()));
        content.setText(Html.fromHtml(item.getText() == null ? "" : item.getText()));
    }

    public static CharSequence trimTrailingWhitespace(CharSequence source) {
        if (source == null)
            return "";
        int i = source.length();
        while (--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }
        return source.subSequence(0, i + 1);
    }
}