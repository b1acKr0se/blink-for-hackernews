package nt.hai.blinkforhackernews.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;

public class DetailActivity extends AnimBaseActivity {
    private TextView textView;

    public static void navigate(Context context, Item item) {
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        textView = (TextView) findViewById(R.id.title);
        readIntent();
    }

    private void readIntent() {
        Intent intent = getIntent();
        Item item = (Item) intent.getExtras().getSerializable("item");
        textView.setText(item.getTitle());
    }
}
