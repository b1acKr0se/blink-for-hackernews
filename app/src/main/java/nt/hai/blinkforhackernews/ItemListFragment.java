package nt.hai.blinkforhackernews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nt.hai.blinkforhackernews.data.remote.HNClient;
import nt.hai.blinkforhackernews.view.adapter.ItemAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListFragment extends Fragment {
    private RecyclerView recyclerView;
    private int[] responseId;
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
        adapter = new ItemAdapter(getContext(), responseId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}