package com.gdg.homework.modules.oneday

import android.view.View
import android.widget.TextView
import com.gdg.homework.R
import com.gdg.homework.modules.common.BaseWeatherActivity
import com.gdg.homework.modules.common.domain.WeatherForecast
import com.gdg.homework.modules.common.formatDate

/**
 * Created by vkirillov on 21.08.2017.
 */
class OneDayActivity: BaseWeatherActivity() {

    private lateinit var mornView: TextView
    private lateinit var dayView: TextView
    private lateinit var eveView: TextView
    private lateinit var nightView: TextView
    private lateinit var dateView: TextView

    override val daysCount: Int
        get() = 1

    override val presentationContentViewResId: Int
        get() = R.layout.item_view

    override fun onForecastLoaded(forecast: WeatherForecast) {
        forecast.list?.firstOrNull()?.let {
            it.temp?.let {
                mornView.text = "Утро: ${it.morn}°C"
                dayView.text = "День: ${it.day}°C"
                eveView.text = "Вечер: ${it.eve}°C"
                nightView.text = "Ночь: ${it.night}°C"
            }

            it.timestamp?.let {
                dateView.text = formatDate(it)
            }
        }
    }

    override fun onPresentationViewInflated(content: View) {
        mornView = content.findViewById(R.id.morn) as TextView
        dayView = content.findViewById(R.id.day) as TextView
        eveView = content.findViewById(R.id.eve) as TextView
        nightView = content.findViewById(R.id.night) as TextView

        dateView = content.findViewById(R.id.date) as TextView
    }

}