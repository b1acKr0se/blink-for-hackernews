package nt.hai.blinkforhackernews.data.remote;

import nt.hai.blinkforhackernews.data.model.Item;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HNClient {
    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0/";
    private static HNClient instance;
    private RetrofitClient retrofitClient;

    private HNClient() {
    }

    public static HNClient getInstance() {
        if (instance == null) {
            instance = new HNClient();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            instance.retrofitClient = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RetrofitClient.class);
        }
        return instance;
    }

    public Call<int[]> getTopStories() {
        return retrofitClient.topStories();
    }

    public Call<Item> getItem(String id) {
        return retrofitClient.getItem(id);
    }
}
