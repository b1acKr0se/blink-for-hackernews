package nt.hai.blinkforhackernews.data.remote;


import nt.hai.blinkforhackernews.data.model.Item;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitClient {
    @GET("topstories.json")
    Call<int[]> topStories();

    @GET("item/{id}.json")
    Call<Item> getItem(@Path("id") String id);
}
