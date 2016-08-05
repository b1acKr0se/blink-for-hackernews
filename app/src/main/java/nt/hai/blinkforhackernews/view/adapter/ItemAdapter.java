package nt.hai.blinkforhackernews.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
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
        if (!item.isLoaded())
            HNClient.getInstance().getItem(item.getId()).enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    Item responseItem = response.body();
                    responseItem.setLoaded(true);
                    items.set(position, responseItem);
                    notifyItemChanged(position);
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {

                }
            });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
