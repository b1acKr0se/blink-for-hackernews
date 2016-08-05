package nt.hai.blinkforhackernews.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nt.hai.blinkforhackernews.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    public static CommentFragment newInstance(String title) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
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
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(getArguments().getString("title"));
        return view;
    }

}
