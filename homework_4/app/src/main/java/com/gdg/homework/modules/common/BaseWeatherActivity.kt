package com.gdg.homework.modules.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.gdg.homework.BuildConfig
import com.gdg.homework.R
import com.gdg.homework.api.Api
import com.gdg.homework.ext.enqueue
import com.gdg.homework.modules.common.domain.WeatherForecast
import retrofit2.Call

/**
 * Created by vkirillov on 21.08.2017.
 */
abstract class BaseWeatherActivity : AppCompatActivity() {

    open val daysCount: Int = 1

    open val presentationContentViewResId: Int = 0

    private var call: Call<WeatherForecast>? = null

    private var errorContainer: ViewGroup? = null
    private var progressView: ProgressBar? = null
    private var presentationContentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        errorContainer = findViewById(R.id.error_container) as ViewGroup
        progressView = findViewById(R.id.progress_bar) as ProgressBar

        findViewById(R.id.try_again).setOnClickListener { getForecast() }

        val root = findViewById(R.id.root) as ViewGroup

        presentationContentView = LayoutInflater.from(this)
                .inflate(presentationContentViewResId, root, false)
        root.addView(presentationContentView,
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT))
        onPresentationViewInflated(presentationContentView!!)

        getForecast()
    }

    private fun getForecast() {
        onStartLoading()
        call?.cancel()
        call = Api.service.getForecast(BuildConfig.KAZAN_CITY_ID, daysCount)
                .enqueue({
                    errorContainer?.visibility = View.GONE
                    presentationContentView?.visibility = View.VISIBLE
                    progressView?.visibility = View.GONE
                    onForecastLoaded(it)
                }, { onLoadingError() })
    }

    open fun onStartLoading() {
        errorContainer?.visibility = View.GONE
        presentationContentView?.visibility = View.GONE
        progressView?.visibility = View.VISIBLE
    }

    open fun onLoadingError() {
        errorContainer?.visibility = View.VISIBLE
        presentationContentView?.visibility = View.GONE
        progressView?.visibility = View.GONE
    }

    abstract fun onForecastLoaded(forecast: WeatherForecast)

    abstract fun onPresentationViewInflated(content: View)
}