package com.gdg.crypto.currencies.modules.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.gdg.crypto.currencies.R
import com.gdg.crypto.currencies.api.Api
import com.gdg.crypto.currencies.ext.safeDispose
import com.gdg.crypto.currencies.modules.detail.CurrencyDetailActivity
import com.gdg.crypto.currencies.modules.detail.domain.CurrencyExtras
import com.gdg.crypto.currencies.widgets.ProgressView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit

/**
 * Created by vkirillov on 23.08.2017.
 */
class CurrencyListActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private lateinit var progressView: ProgressView
    private val adapter = CurrencyListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_list)

        progressView = findViewById(R.id.progress_view)
        progressView.setOnRetryListener { getCurrencyList(isRetry = true) }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            it.setDisplayHomeAsUpEnabled(true)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    adapter.resumeImagesLoading() else adapter.pauseImagesLoading()
            }
        })

        getCurrencyList(false)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun getCurrencyList(isRetry: Boolean) {
        disposable.safeDispose()

        progressView.showProgress(withAnimation = !isRetry)
        disposable = Api.currencyListService.getCurrencyList()
                .subscribeOn(Schedulers.io())
                .flatMap { res ->
                    Single.just(res.currencies?.values?.toList()
                            ?.sortedBy { it.sortOrder?.toInt() }
                            ?.map {
                                it.apply {
                                    imageUrl = res.baseImageUrl + it.imageUrl
                                }
                            } ?: listOf())
                }
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.items = it
                    adapter.notifyDataSetChanged()
                    progressView.hideProgress(true)
                }, { progressView.error(getString(R.string.error_cant_load_data)) })
    }

    @Subscribe fun onCurrencySelected(event: OnCurrencySelectedEvent) {
        val currency = event.info.name ?: return
        val coinName = event.info.coinName ?: return
        startActivity(Intent(this, CurrencyDetailActivity::class.java).apply {
            putExtras(CurrencyExtras(currency, coinName).toBundle())
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    override fun onDestroy() {
        disposable.safeDispose()
        super.onDestroy()
    }
}