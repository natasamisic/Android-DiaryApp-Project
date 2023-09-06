package com.example.diaryapp.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {

    String URL = "https://free-quotes-api.herokuapp.com";

    @GET("/")
    Call<Quotes> getRandomQuote();
}
