package nt.hai.blinkforhackernews;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.data.remote.HNClient;
import nt.hai.blinkforhackernews.utility.HardwareUtils;
import nt.hai.blinkforhackernews.view.DetailActivity;
import nt.hai.blinkforhackernews.view.ItemClickListener;
import nt.hai.blinkforhackernews.view.OnTitleClickListener;
import nt.hai.blinkforhackernews.view.adapter.ItemAdapter;
import nt.hai.blinkforhackernews.view.custom.SimpleDividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListFragment extends Fragment implements OnTitleClickListener, SwipeRefreshLayout.OnRefreshListener, ItemClickListener {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int[] responseId;
    private List<Item> itemList = new ArrayList<>();
    private ItemAdapter adapter;

    public ItemListFragment() {
    }

    static ItemListFragment newInstance() {
        return new ItemListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView.setClipToPadding(false);
        setUpPadding();
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), android.R.color.white));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
        return view;
    }

    private void setUpPadding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (HardwareUtils.hasSoftKeys(getActivity().getWindowManager())) {
                recyclerView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.recycler_padding_bottom));
            } else {
                recyclerView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.normal_padding_bottom));
            }
        }
    }

    private void request() {
        recyclerView.setVisibility(View.GONE);
        Call<int[]> call = HNClient.getInstance().getTopStories();
        call.enqueue(new Callback<int[]>() {
            @Override
            public void onResponse(Call<int[]> call, Response<int[]> response) {
                responseId = response.body();
                if (responseId == null)
                    return;
                itemList.clear();
                for (int aResponseId : responseId) {
                    Item item = new Item();
                    item.setId(String.valueOf(aResponseId));
                    itemList.add(item);
                }
                showList();
            }

            @Override
            public void onFailure(Call<int[]> call, Throwable t) {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private void showList() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new ItemAdapter(getContext(), itemList);
            adapter.setItemClickListener(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), null));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTitleClick() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        request();
    }

    @Override
    public void onItemClick(int position) {
        Item item = itemList.get(position);
        if (!item.isLoaded()) return;
        DetailActivity.navigate(getContext(), item);
    }
}