package nt.hai.blinkforhackernews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.data.remote.HNClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Call<int[]> call = HNClient.getInstance().getTopStories();
        call.enqueue(new Callback<int[]>() {
            @Override
            public void onResponse(Call<int[]> call, Response<int[]> response) {
                int[] result = response.body();
                for (int i : result) {
                    Log.i(TAG, "onResponse: " + i);
                }
            }

            @Override
            public void onFailure(Call<int[]> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
        Call<Item> call1 = HNClient.getInstance().getItem("9129911");
        call1.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Log.i(TAG, "onResponse: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

            }
        });
    }
}
