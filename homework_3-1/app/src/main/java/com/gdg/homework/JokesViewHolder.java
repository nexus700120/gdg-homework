package com.gdg.homework;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vkirillov on 18.08.2017.
 */

public class JokesViewHolder extends RecyclerView.ViewHolder {

    private TextView mJokeText;

    public JokesViewHolder(View itemView) {
        super(itemView);
        mJokeText = (TextView) itemView.findViewById(R.id.text);
    }

    public void bind(@Nullable String joke) {
        if (joke == null || joke.isEmpty()) {
            mJokeText.setText(null);
            return;
        }
        //noinspection deprecation
        mJokeText.setText(Html.fromHtml(joke));
    }
}
