package com.gdg.crypto.currencies.modules.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.gdg.crypto.currencies.R
import com.gdg.crypto.currencies.api.Api
import com.gdg.crypto.currencies.ext.afterMeasured
import com.gdg.crypto.currencies.ext.safeDispose
import com.gdg.crypto.currencies.modules.detail.domain.CurrencyExtras
import com.gdg.crypto.currencies.modules.detail.domain.HistoryResponse
import com.gdg.crypto.currencies.widgets.ProgressView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by vkirillov on 23.08.2017.
 */
class CurrencyDetailActivity : AppCompatActivity() {

    private val fiatCurrency = "EUR"
    private val fiatCurrencySymbol = "€"

    private var disposable: Disposable? = null

    private lateinit var rateContainer: View
    private lateinit var rateView: TextView
    private lateinit var progressView: ProgressView
    private lateinit var chartView: LineChart
    private lateinit var share: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val extras = CurrencyExtras.fromArgs(intent.extras)
        val coinName = extras.coinName
        val currency = extras.currency

        if (coinName.isNullOrEmpty() || currency.isNullOrEmpty()) {
            finish()
            return
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.title = coinName

        rateContainer = findViewById(R.id.rate_container)
        rateView = findViewById(R.id.rate)
        progressView = findViewById(R.id.progress_view)
        progressView.setOnRetryListener { getFinancialData(currency!!, true) }

        chartView = findViewById(R.id.bar_chart)

        chartView.description.isEnabled = false
        chartView.setDrawBorders(false)
        chartView.setTouchEnabled(false)
        chartView.isAutoScaleMinMaxEnabled = true

        chartView.legend.isEnabled = false

        chartView.xAxis.isEnabled = false
        chartView.axisLeft.isEnabled = false
        chartView.axisRight.isEnabled = false

        getFinancialData(currency!!, false)

        rateContainer.afterMeasured {
            rateContainer.translationY = (-(rateContainer.height - toolbar.height)).toFloat()
        }

        share = findViewById(R.id.share)
        share.setOnClickListener { share() }
    }

    private fun getFinancialData(cryptoCurrency: String, isRetry: Boolean) {
        disposable.safeDispose()

        progressView.showProgress(withAnimation = !isRetry)
        disposable = Single.zip(Api.financialService.getRates(cryptoCurrency, fiatCurrency),
                Api.financialService.getHistory(cryptoCurrency, fiatCurrency, 6),
                BiFunction { t1: Map<String, Float>, t2: HistoryResponse -> Pair(t1, t2) })
                .subscribeOn(Schedulers.io())
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    progressView.hideProgress(true)
                    onDataReceived(cryptoCurrency, it)
                }, { progressView.error(getString(R.string.error_cant_load_data)) })
    }

    private fun onDataReceived(cryptoCurrency: String, data: Pair<Map<String, Float>, HistoryResponse>) {
        bindHistoricalData(data.second)

        data.first.values.firstOrNull()?.let {
            rateView.text = getFormattedRate(cryptoCurrency, it)
        }

        val rateContainerAnimator = ObjectAnimator.ofFloat(rateContainer,
                View.TRANSLATION_Y, rateContainer.translationY, 0F)

        val rateAnimator = ObjectAnimator.ofFloat(rateView, View.ALPHA, 0F, 1F).apply {
            startDelay = 100
        }

        chartView.visibility = View.VISIBLE
        chartView.alpha = 0F
        val chartAnimator = ObjectAnimator.ofFloat(chartView, View.ALPHA, 0F, 1F).apply {
            startDelay = 100
        }

        share.visibility = View.VISIBLE
        share.alpha = 0F
        val shareAnimator = ObjectAnimator.ofFloat(share, View.ALPHA, 0F, 1F)

        val animatorSet = AnimatorSet()
        animatorSet.play(rateContainerAnimator).with(rateAnimator)
        animatorSet.play(chartAnimator).with(rateAnimator)
        animatorSet.play(shareAnimator).with(rateContainerAnimator)
        animatorSet.start()
    }

    private fun bindHistoricalData(res: HistoryResponse) {
        val entries = mutableListOf<Entry>()
        res.data?.sortedBy { it.time }
                ?.forEachIndexed { index, historyItem ->
                    entries.add(BarEntry(index.toFloat(), historyItem.close ?: 0F))
                }

        val set = LineDataSet(entries, null)
        set.color = ContextCompat.getColor(this, R.color.colorAccent)
        set.setDrawFilled(true)
        set.valueTextSize = 9f
        set.fillDrawable = getDrawable(R.drawable.chart_gradient)

        val data = LineData(set)
        chartView.data = data
        chartView.invalidate()
    }


    private fun getFormattedRate(cryptoCurrency: String, fiatAmount: Float): String {
        val isRussian = Locale.getDefault().language == "ru"
        val formattedFiat = DecimalFormat("###,###,###,##0.00000000").format(fiatAmount)

        return if (isRussian) {
            "1 $cryptoCurrency = $formattedFiat $fiatCurrencySymbol"
        } else {
            "${cryptoCurrency}1 = $fiatCurrencySymbol$formattedFiat"
        }
    }

    private fun share() {
        val shareText = rateView.text
        if (shareText.isNullOrEmpty()) {
            return
        }
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    override fun onDestroy() {
        disposable.safeDispose()
        super.onDestroy()
    }
}