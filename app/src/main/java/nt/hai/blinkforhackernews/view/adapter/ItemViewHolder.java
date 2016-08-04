package nt.hai.blinkforhackernews.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import nt.hai.blinkforhackernews.R;

class ItemViewHolder extends RecyclerView.ViewHolder {
    private TextView idTextView;

    ItemViewHolder(View itemView) {
        super(itemView);
        idTextView = (TextView) itemView.findViewById(R.id.item_id);
    }

    void bind(int id) {
        idTextView.setText(String.valueOf(id));
    }
}
