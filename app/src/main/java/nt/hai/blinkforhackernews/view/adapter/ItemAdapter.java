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
import nt.hai.blinkforhackernews.view.ItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private Context context;
    private List<Item> items;
    private ItemClickListener itemClickListener;
    private List<String> currentLoadingList;

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
        this.currentLoadingList = new ArrayList<>();
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hn_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        Item item = items.get(position);
        holder.bind(item, position, itemClickListener);
        if (!item.isLoaded() && !currentLoadingList.contains(item.getId())) {
            currentLoadingList.add(item.getId());
            HNClient.getInstance().getItem(item.getId()).enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    Item responseItem = response.body();
                    responseItem.setLoaded(true);
                    items.set(position, responseItem);
                    notifyItemChanged(position);
                    currentLoadingList.remove(responseItem.getId());
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {

                }
            });
        }
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    public void updateItem(int position) {
        items.get(position).setTitle("Upvoted!");
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
