package com.gdg.homework.modules.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gdg.homework.R
import com.gdg.homework.modules.chart.ChartActivity
import com.gdg.homework.modules.oneday.OneDayActivity
import com.gdg.homework.modules.recycler.RecyclerActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.one_day).setOnClickListener {
            startActivity(Intent(this, OneDayActivity::class.java))
        }

        findViewById(R.id.recycler).setOnClickListener {
            startActivity(Intent(this, RecyclerActivity::class.java))
        }

        findViewById(R.id.chart).setOnClickListener {
            startActivity(Intent(this, ChartActivity::class.java))
        }
    }
}
