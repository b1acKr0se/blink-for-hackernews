package nt.hai.blinkforhackernews.view;


import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.utility.HardwareUtils;
import nt.hai.blinkforhackernews.view.adapter.CommentAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment implements OnMenuCommentClickListener {
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
            adapter = new CommentAdapter(getContext(), recyclerView, commentList);
            adapter.setOnMenuCommentClickListener(this);
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            recyclerView.getItemAnimator().setAddDuration(0);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    private void setUpPadding() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getBoolean("pref_has_softkey", false)) {
                recyclerView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.normal_padding_bottom), 0, getResources().getDimensionPixelSize(R.dimen.recycler_padding_bottom));
            } else {
                recyclerView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.normal_padding_bottom), 0, getResources().getDimensionPixelSize(R.dimen.normal_padding_bottom));
            }
        }
    }

    @Override
    public void onUpvote(int position) {
        Toast.makeText(getActivity(), "Upvoted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProfile(int position) {

    }

    @Override
    public void onReply(int position) {

    }

    @Override
    public void onCopy(int position) {

    }
}
