package nt.hai.blinkforhackernews.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.utility.DateUtils;
import nt.hai.blinkforhackernews.utility.LinkUtils;
import nt.hai.blinkforhackernews.view.ItemClickListener;
import nt.hai.blinkforhackernews.view.custom.MultiSizeTextView;

class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final long CLICK_TIME_INTERVAL = 500;
    private MultiSizeTextView titleAndLinkTextView;
    private TextView authorTextView, timeTextView, scoreTextView, commentTextView;
    private ItemClickListener listener;
    private int position;
    private long lastClickTime = System.currentTimeMillis();

    ItemViewHolder(View itemView) {
        super(itemView);
        titleAndLinkTextView = (MultiSizeTextView) itemView.findViewById(R.id.item_title_link);
        authorTextView = (TextView) itemView.findViewById(R.id.item_author);
        timeTextView = (TextView) itemView.findViewById(R.id.item_time);
        scoreTextView = (TextView) itemView.findViewById(R.id.item_score);
        commentTextView = (TextView) itemView.findViewById(R.id.item_comment);
    }

    void bind(Item item, int position, ItemClickListener listener) {
        this.listener = listener;
        this.position = position;
        if (!item.isLoaded()) {
            titleAndLinkTextView.setTitle("-").setLink(LinkUtils.getHostName("-")).draw();
            authorTextView.setText("-");
            timeTextView.setText("-");
            scoreTextView.setText("-");
            commentTextView.setText("-");
            return;
        }
        itemView.setOnClickListener(this);
        titleAndLinkTextView.setTitle(item.getTitle()).setLink(LinkUtils.getHostName(item.getUrl())).draw();
        authorTextView.setText(item.getBy());
        timeTextView.setText(DateUtils.getReadableDate(item.getTime()));
        scoreTextView.setText(String.valueOf(item.getScore()));
        commentTextView.setText(String.valueOf(item.getDescendants()));
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            long now = System.currentTimeMillis();
            if (now - lastClickTime < CLICK_TIME_INTERVAL) {
                return;
            }
            lastClickTime = now;
            listener.onItemClick(view, position);
        }
    }
}
