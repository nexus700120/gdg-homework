package com.gdg.homework.modules.chart

import android.view.View
import com.gdg.homework.R
import com.gdg.homework.modules.common.BaseWeatherActivity
import com.gdg.homework.modules.common.domain.WeatherForecast
import com.gdg.homework.modules.common.getNumberOfMonth
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

/**
 * Created by vkirillov on 21.08.2017.
 */
class ChartActivity : BaseWeatherActivity() {

    override val daysCount: Int
        get() = 7

    override val presentationContentViewResId: Int
        get() = R.layout.activity_chart

    private lateinit var chart: BarChart

    override fun onForecastLoaded(forecast: WeatherForecast) {
        val entries = mutableListOf<BarEntry>()
        forecast.list?.filter { it.timestamp != null && it.temp != null }
                ?.forEach {
                    val dateNumber = getNumberOfMonth(it.timestamp!!)
                    val t = it.temp?.day ?: 0.0F
                    entries.add(BarEntry(dateNumber, t))
                }

        val set = BarDataSet(entries, "Температура")

        val data = BarData(set)
        data.barWidth = 0.9f
        chart.data = data
        chart.invalidate()
    }

    override fun onPresentationViewInflated(content: View) {
        chart = content as BarChart
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setMaxVisibleValueCount(60)
        chart.setDrawGridBackground(false)
        chart.setFitBars(true)
    }

}