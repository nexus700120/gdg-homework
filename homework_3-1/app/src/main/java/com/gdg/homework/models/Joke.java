package com.gdg.homework.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vkirillov on 18.08.2017.
 */
public class Joke {
    //@formatter:off
    private @SerializedName("id") int id;
    private @SerializedName("joke") String joke;
    //@formatter:on

    public int getId() {
        return id;
    }

    public @Nullable String getJoke() {
        return joke;
    }

    public void setJoke(@Nullable String joke) {
        this.joke = joke;
    }
}
