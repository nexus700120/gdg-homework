package com.gdg.homework.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vkirillov on 18.08.2017.
 */
public class JokesResponse {

    // @formatter:off
    private @SerializedName("type") String type;
    private @SerializedName("value") List<Joke> value;
    // @formatter:on


    public @Nullable String getType() {
        return type;
    }

    public @Nullable List<Joke> getValue() {
        return value;
    }
}
