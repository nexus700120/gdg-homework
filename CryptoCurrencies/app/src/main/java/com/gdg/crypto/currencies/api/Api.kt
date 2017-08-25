package com.gdg.crypto.currencies.api

import com.gdg.crypto.currencies.BuildConfig
import com.gdg.crypto.currencies.modules.common.domain.CurrencyListResponse
import com.gdg.crypto.currencies.modules.detail.domain.HistoryResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Created by vkirillov on 23.08.2017.
 */
interface CurrencyService {

    @GET("api/data/coinlist")
    fun getCurrencyList(): Single<CurrencyListResponse>
}

interface FinancialService {

    @GET("data/histoday")
    fun getHistory(@Query("fsym") cryptoCurrency: String,
                   @Query("tsym") fiatCurrency: String,
                   @Query("limit") limit: Int): Single<HistoryResponse>

    @GET("data/price")
    fun getRates(@Query("fsym") cryptoCurrency: String,
                 @Query("tsyms") fiatCurrencies: String): Single<Map<String, Float>>
}

object Api {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    val currencyListService: CurrencyService by lazy {
        Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_ENDPOINT_CURRENCY_LIST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(CurrencyService::class.java)
    }

    val financialService: FinancialService by lazy {
        Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_ENDPOINT_FINANCIAL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(FinancialService::class.java)
    }
}