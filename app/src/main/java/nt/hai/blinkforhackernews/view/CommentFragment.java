package nt.hai.blinkforhackernews.view;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.utility.HardwareUtils;
import nt.hai.blinkforhackernews.view.adapter.CommentAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<Item> commentList = new ArrayList<>();


    public static CommentFragment newInstance(Item item) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        setUpPadding();
        Item item = (Item) getArguments().getSerializable("item");
        commentList.add(item);
        if (item.getKids() != null && item.getKids().length > 0) {
            for (int i = 0; i < item.getKids().length; i++) {
                Item comment = new Item();
                comment.setId(String.valueOf(item.getKids()[i]));
                comment.setLevel(0);
                commentList.add(comment);
            }
            CommentAdapter adapter = new CommentAdapter(getContext(), commentList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    private void setUpPadding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (HardwareUtils.hasSoftKeys(getActivity().getWindowManager())) {
                recyclerView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.normal_padding_bottom), 0, getResources().getDimensionPixelSize(R.dimen.recycler_padding_bottom));
            } else {
                recyclerView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.normal_padding_bottom), 0, getResources().getDimensionPixelSize(R.dimen.normal_padding_bottom));
            }
        }
    }
}