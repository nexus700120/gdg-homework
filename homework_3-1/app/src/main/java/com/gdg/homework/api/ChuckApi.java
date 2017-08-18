package com.gdg.homework.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vkirillov on 18.08.2017.
 */
public class ChuckApi {

    private static final ChuckApi ourInstance = new ChuckApi();

    private final Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl("http://api.icndb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final ChuckService mService = mRetrofit.create(ChuckService.class);

    private ChuckApi() {}

    static ChuckApi getInstance() {
        return ourInstance;
    }

    ChuckService getService() {
        return mService;
    }
}
