package nt.hai.blinkforhackernews.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.data.remote.HNClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_COMMENT = 1;
    private List<String> currentLoadingList;
    private List<Item> items;
    private Context context;

    public CommentAdapter(Context context, List<Item> list) {
        this.context = context;
        this.items = list;
        this.currentLoadingList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (!items.get(position).isLoaded())
            return TYPE_COMMENT;
        if (items.get(position).getType() != null && items.get(position).getType().equals("comment"))
            return TYPE_COMMENT;
        return TYPE_HEADER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_COMMENT)
            return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hn_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Item item = items.get(position);
        if (viewHolder instanceof CommentViewHolder) {
            CommentViewHolder holder = (CommentViewHolder) viewHolder;
            final int level = item.getLevel();
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            int marginValue = level * context.getResources().getDimensionPixelSize(R.dimen.view_level_indicator);
            int marginComment = context.getResources().getDimensionPixelSize(R.dimen.padding_card_comment);
            params.setMargins(marginValue, 0, 0, marginComment);
            holder.itemView.setLayoutParams(params);
            holder.bind(item);
            if (!item.isLoaded() && !currentLoadingList.contains(item.getId())) {
                currentLoadingList.add(item.getId());
                HNClient.getInstance().getItem(item.getId()).enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        Item responseItem = response.body();
                        responseItem.setLoaded(true);
                        responseItem.setLevel(level);
                        currentLoadingList.remove(responseItem.getId());
                        int currentPosition = items.indexOf(item);
                        items.set(currentPosition, responseItem);
                        notifyItemChanged(currentPosition);
                        int pos = currentPosition + 1;
                        int index;
                        if (responseItem.getKids() != null && responseItem.getKids().length > 0) {
                            for (index = 0; index < responseItem.getKids().length; index++) {
                                Item kid = new Item();
                                kid.setId(String.valueOf(responseItem.getKids()[index]));
                                kid.setLevel(level + 1);
                                items.add(pos, kid);
                                if (pos < getItemCount())
                                    notifyItemInserted(pos);
                                pos++;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {

                    }
                });
            }
        } else if (viewHolder instanceof ItemViewHolder) {
            ((ItemViewHolder) viewHolder).bind(item, 0, null);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
