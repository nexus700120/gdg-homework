package com.gdg.homework.modules.recycler

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gdg.homework.R
import com.gdg.homework.modules.common.BaseWeatherActivity
import com.gdg.homework.modules.common.domain.WeatherForecast

/**
 * Created by vkirillov on 21.08.2017.
 */
class RecyclerActivity : BaseWeatherActivity() {

    override val daysCount: Int
        get() = 7

    override val presentationContentViewResId: Int
        get() = R.layout.activity_recycler

    private val adapter = Adapter()

    override fun onForecastLoaded(forecast: WeatherForecast) {
        adapter.forecast = forecast
        adapter.notifyDataSetChanged()
    }

    override fun onPresentationViewInflated(content: View) {
        val recycler = content.findViewById(R.id.recycler) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val decorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider))
        recycler.addItemDecoration(decorator)
    }

}