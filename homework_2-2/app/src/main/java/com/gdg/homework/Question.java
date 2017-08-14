package com.gdg.homework;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vkirillov on 14.08.2017.
 */

public class Question {
    private @SerializedName("question") String mQuestion;
    private @SerializedName("answers") List<String> mAnswers;
    private @SerializedName("correct") String mCorrect;

    public String getQuestion() {
        return mQuestion;
    }

    public List<String> getAnswers() {
        return mAnswers;
    }

    public String getCorrect() {
        return mCorrect;
    }
}
