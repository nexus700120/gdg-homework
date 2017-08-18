package com.gdg.homework.api;

import com.gdg.homework.models.JokesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by vkirillov on 18.08.2017.
 */
interface ChuckService {

    @GET("/jokes/random/{count}")
    Call<JokesResponse> getRandomJokes(@Path("count") int count);
}
