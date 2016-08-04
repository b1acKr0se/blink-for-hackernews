package nt.hai.blinkforhackernews.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.utility.DateUtils;
import nt.hai.blinkforhackernews.utility.UrlUtils;
import nt.hai.blinkforhackernews.view.custom.MultiSizeTextView;

class ItemViewHolder extends RecyclerView.ViewHolder {
    private MultiSizeTextView titleAndLinkTextView;
    private TextView authorTextView, timeTextView, scoreTextView, commentTextView;


    ItemViewHolder(View itemView) {
        super(itemView);
        titleAndLinkTextView = (MultiSizeTextView) itemView.findViewById(R.id.item_title_link);
        authorTextView = (TextView) itemView.findViewById(R.id.item_author);
        timeTextView = (TextView) itemView.findViewById(R.id.item_time);
        scoreTextView = (TextView) itemView.findViewById(R.id.item_score);
        commentTextView = (TextView) itemView.findViewById(R.id.item_comment);
    }

    void bind(Item item) {
        if (!item.isLoaded()) {
           titleAndLinkTextView.setTitle("-").setLink(UrlUtils.getHostName("-")).draw();
            authorTextView.setText("-");
            timeTextView.setText("-");
            scoreTextView.setText("-");
            commentTextView.setText("-");
            return;
        }
        titleAndLinkTextView.setTitle(item.getTitle()).setLink(UrlUtils.getHostName(item.getUrl())).draw();
        authorTextView.setText(item.getBy());
        timeTextView.setText(DateUtils.getReadableDate(item.getTime()));
        scoreTextView.setText(String.valueOf(item.getScore()));
        commentTextView.setText(String.valueOf(item.getDescendants()));
    }
}
