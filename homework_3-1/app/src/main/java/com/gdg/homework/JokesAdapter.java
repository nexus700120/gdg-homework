package com.gdg.homework;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gdg.homework.models.Joke;

import java.util.List;

/**
 * Created by vkirillov on 18.08.2017.
 */

public class JokesAdapter extends RecyclerView.Adapter<JokesViewHolder> {

    private List<Joke> mJokeList;

    @Override
    public JokesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new JokesViewHolder(layoutInflater.inflate(R.layout.joke_item, parent, false));
    }

    @Override
    public void onBindViewHolder(JokesViewHolder holder, int position) {
        holder.bind(mJokeList.get(position).getJoke());
    }

    @Override
    public int getItemCount() {
        return mJokeList == null ? 0 : mJokeList.size();
    }

    public void replace(List<Joke> jokeList) {
        mJokeList = jokeList;
    }
}
