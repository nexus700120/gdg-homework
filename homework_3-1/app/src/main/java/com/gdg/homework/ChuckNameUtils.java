package com.gdg.homework;

import android.support.annotation.Nullable;

/**
 * Created by vkirillov on 18.08.2017.
 */
public class ChuckNameUtils {

    public @Nullable static String replaceChuckName(@Nullable String joke) {
        if (joke == null || joke.isEmpty()) {
            return joke;
        }
        return joke.replaceAll("Chuck", "Vitaly")
                .replaceAll("Norris", "Kirillov");
    }
}
