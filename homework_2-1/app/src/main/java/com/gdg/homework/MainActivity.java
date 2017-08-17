package com.gdg.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTextView = (TextView) findViewById(R.id.result);

        findViewById(R.id.add_1).setOnClickListener(this);
        findViewById(R.id.add_3).setOnClickListener(this);
        findViewById(R.id.add_5).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
    }

    private
    @Nullable
    Integer getCurrentNumber() {
        String numberStrRepresentation = mResultTextView.getText().toString();
        try {
            return Integer.valueOf(numberStrRepresentation);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void add(int number) {
        Integer currentNumber = getCurrentNumber();
        if (currentNumber == null) {
            return;
        }
        currentNumber += number;
        mResultTextView.setText(String.valueOf(currentNumber));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_1:
                add(1);
                break;
            case R.id.add_3:
                add(3);
                break;
            case R.id.add_5:
                add(5);
                break;
            case R.id.clear:
                mResultTextView.setText("0");
                break;
        }
    }
}
