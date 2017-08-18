package com.gdg.homework.api;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.gdg.homework.ChuckNameUtils;
import com.gdg.homework.models.Joke;
import com.gdg.homework.models.JokesResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by vkirillov on 18.08.2017.
 */
public class GetJokesAsyncTask extends AsyncTask<Integer, Void, ResponseWrapper<JokesResponse>> {


    public interface GetJokesCallback {
        void onSuccess(@NonNull JokesResponse jokesResponse);
        void onFailed(Throwable t);
    }

    private GetJokesCallback mCallback;

    public GetJokesAsyncTask(GetJokesCallback callback) {
        mCallback = callback;
    }

    @Override
    protected ResponseWrapper<JokesResponse> doInBackground(Integer... params) {
        Call<JokesResponse> call = ChuckApi.getInstance().getService().getRandomJokes(params[0]);
        try {
            Response<JokesResponse> response = call.execute();
            JokesResponse jokesResponse = response.body();
            if (jokesResponse == null) {
                return new ResponseWrapper<>(new NullPointerException("jokesResponse == null"));
            }
            List<Joke> jokeList = jokesResponse.getValue();
            if (jokeList != null && !jokeList.isEmpty()) {
                for (Joke joke : jokeList) {
                    joke.setJoke(ChuckNameUtils.replaceChuckName(joke.getJoke()));
                }
            }
            return new ResponseWrapper<>(jokesResponse);
        } catch (IOException e) {
            return new ResponseWrapper<>(e);
        }
    }

    @Override
    protected void onPostExecute(ResponseWrapper<JokesResponse> responseWrapper) {
        if (isCancelled() || mCallback == null) {
            return;
        }

        if (responseWrapper.isSuccess()) {
            mCallback.onSuccess(responseWrapper.getResponse());
        } else {
            mCallback.onFailed(responseWrapper.getThrowable());
        }
    }
}
