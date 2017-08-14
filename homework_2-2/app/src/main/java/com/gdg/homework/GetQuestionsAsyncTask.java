package com.gdg.homework;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vkirillov on 14.08.2017.
 */

public class GetQuestionsAsyncTask extends AsyncTask<Void, Void, List<Question>> {

    public interface OnGetQuestionsListener {
        void onLoaded(List<Question> questionList);
    }

    private AssetManager mAssetsManager;
    private OnGetQuestionsListener mListener;

    public GetQuestionsAsyncTask(AssetManager assetManager, OnGetQuestionsListener listener) {
        mAssetsManager = assetManager;
        mListener = listener;
    }

    @Override
    protected List<Question> doInBackground(Void... params) {
        String questionContent = getFileContentFromAssets();
        if (questionContent == null || questionContent.isEmpty()) {
            return null;
        }
        Type listType = new TypeToken<ArrayList<Question>>() {}.getType();
        return new Gson().fromJson(questionContent, listType);
    }

    @Override
    protected void onPostExecute(List<Question> questions) {
        if (isCancelled()) return;
        mListener.onLoaded(questions);
    }

    @Nullable
    private String getFileContentFromAssets() {
        StringBuilder buf = new StringBuilder();
        try {
            InputStream json = mAssetsManager.open("questions.json");
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                buf.append(line);
            }
            in.close();
            return buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
