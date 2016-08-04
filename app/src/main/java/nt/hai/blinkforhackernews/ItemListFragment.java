package nt.hai.blinkforhackernews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.data.remote.HNClient;
import nt.hai.blinkforhackernews.view.adapter.ItemAdapter;
import nt.hai.blinkforhackernews.view.custom.SimpleDividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListFragment extends Fragment {
    private RecyclerView recyclerView;
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
        Call<int[]> call = HNClient.getInstance().getTopStories();
        call.enqueue(new Callback<int[]>() {
            @Override
            public void onResponse(Call<int[]> call, Response<int[]> response) {
                responseId = response.body();
                if (responseId == null)
                    return;
                for (int aResponseId : responseId) {
                    Item item = new Item();
                    item.setId(String.valueOf(aResponseId));
                    itemList.add(item);
                }
                showList();
            }

            @Override
            public void onFailure(Call<int[]> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return view;
    }

    private void showList() {
        adapter = new ItemAdapter(getContext(), itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), null));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}