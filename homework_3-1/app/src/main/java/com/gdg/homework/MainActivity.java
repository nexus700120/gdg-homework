package com.gdg.homework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.gdg.homework.api.GetJokesAsyncTask;
import com.gdg.homework.models.JokesResponse;

public class MainActivity extends AppCompatActivity {

    private final static int NUMBER_OF_JOKES = 30;

    private ProgressBar mProgressBarView;
    private RecyclerView mRecyclerView;
    private View errorContainer;

    private GetJokesAsyncTask mJokesAsyncTask;
    private JokesAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBarView = (ProgressBar) findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        errorContainer = findViewById(R.id.error_container);

        mRecyclerAdapter = new JokesAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        DividerItemDecoration decoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        mRecyclerView.addItemDecoration(decoration);

        findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJokes();
            }
        });

        loadJokes();
    }

    private void loadJokes() {
        if (mJokesAsyncTask != null && !mJokesAsyncTask.isCancelled()) {
            mJokesAsyncTask.cancel(true);
        }

        showProgress();
        mJokesAsyncTask = new GetJokesAsyncTask(new GetJokesAsyncTask.GetJokesCallback() {
            @Override
            public void onSuccess(@NonNull JokesResponse jokesResponse) {
                showRecycler();
                mRecyclerAdapter.replace(jokesResponse.getValue());
                mRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Throwable t) {
                showError();
            }
        });
        mJokesAsyncTask.execute(NUMBER_OF_JOKES);
    }

    private void showProgress() {
        errorContainer.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mProgressBarView.setVisibility(View.VISIBLE);
    }

    private void showRecycler() {
        errorContainer.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBarView.setVisibility(View.GONE);
    }

    private void showError() {
        errorContainer.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mProgressBarView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (mJokesAsyncTask != null) {
            mJokesAsyncTask.cancel(true);
        }
        super.onDestroy();
    }
}
