package com.gdg.homework;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int PAUSE_TIME = 2000;

    private GetQuestionsAsyncTask mAsyncTask;
    private List<Question> mQuestionList;
    private int mQuestIndex = 0;
    private int mCorrectAnswers = 0;
    private int mIncorrectAnswers = 0;

    private TextView mQuestionView;
    private List<Button> mButtonList = new ArrayList<>();
    private TextView mCorrectTextView;
    private TextView mIncorrectTextView;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionView = (TextView) findViewById(R.id.question);

        mButtonList.add((Button) findViewById(R.id.button_1));
        mButtonList.add((Button) findViewById(R.id.button_2));
        mButtonList.add((Button) findViewById(R.id.button_3));
        mButtonList.add((Button) findViewById(R.id.button_4));

        mCorrectTextView = (TextView) findViewById(R.id.correct_answer);
        mIncorrectTextView = (TextView) findViewById(R.id.incorrect_answer);
        updateCounters();

        mAsyncTask = new GetQuestionsAsyncTask(getAssets(),
                new GetQuestionsAsyncTask.OnGetQuestionsListener() {
            @Override
            public void onLoaded(List<Question> questionList) {
                if (questionList == null || questionList.isEmpty()) {
                    finish();
                    return;
                }
                mQuestionList = questionList;
                Collections.shuffle(mQuestionList);
                bindQuestion();
            }
        });
        mAsyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    private void bindQuestion() {
        if (mQuestIndex > mQuestionList.size() - 1) {
            return;
        }

        final Question question = mQuestionList.get(mQuestIndex);
        mQuestionView.setText(question.getQuestion());

        List<String> answerList = question.getAnswers();

        for (int i = 0; i < answerList.size(); i++) {
            final String answer = answerList.get(i);
            final Button button = mButtonList.get(i);
            button.setText(answer);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAnswerSelected(answer.equals(question.getCorrect()), button);
                }
            });
        }
    }

    private void onAnswerSelected(boolean isCorrectAnswer, Button button) {
        buttonsEnabled(false);
        button.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        if (isCorrectAnswer) {
            startPendingCorrectTask(button);
        } else startPendingIncorrectTask(button);
    }

    private void startPendingCorrectTask(final Button button) {
        //noinspection ConstantConditions
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                mCorrectAnswers++;
                next();
            }
        }, PAUSE_TIME);

    }

    private void startPendingIncorrectTask(final Button button) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                mIncorrectAnswers++;
                next();
            }
        }, PAUSE_TIME);
    }

    private void next() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetButtonsColors();
                updateCounters();
                mQuestIndex++;
                if (mQuestIndex >= mQuestionList.size()) {
                    return;
                }
                buttonsEnabled(true);
                bindQuestion();
            }
        }, 1000L);

    }

    private void buttonsEnabled(boolean enabled) {
        for (Button b : mButtonList) {
            b.setClickable(enabled);
        }
    }

    private void resetButtonsColors() {
        for (Button b : mButtonList) {
            b.getBackground().clearColorFilter();
        }
    }

    @SuppressLint("DefaultLocale")
    private void updateCounters() {
        mCorrectTextView.setText(String.format("ПРАВИЛЬНО %d", mCorrectAnswers));
        mIncorrectTextView.setText(String.format("НЕПРАВИЛЬНО %d", mIncorrectAnswers));
    }
}
