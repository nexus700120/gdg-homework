package com.gdg.homework.api

import com.gdg.homework.BuildConfig
import com.gdg.homework.modules.common.domain.WeatherForecast
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException

/**
 * Created by vkirillov on 21.08.2017.
 */
interface WeatherService {

    @GET("data/2.5/forecast/daily")
    fun getForecast(@Query("id") cityId: Int, @Query("cnt") daysCount: Int):
            Call<WeatherForecast>
}

object Api {

    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(UnitsInterceptor())
            .build()

    val service: WeatherService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_ENDPOINT)
            .client(okHttpClient)
            .build()
            .create(WeatherService::class.java)

}

class AuthInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if ("GET".equals(request.method(), true)) {
            val newUrl = request.url()
                    .newBuilder()
                    .addQueryParameter("appid", BuildConfig.API_KEY)
                    .build()

            return chain.proceed(request
                    .newBuilder()
                    .url(newUrl)
                    .build())
        }
        return chain.proceed(chain.request())
    }
}

class UnitsInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if ("GET".equals(request.method(), true)) {
            val newUrl = request.url()
                    .newBuilder()
                    .addQueryParameter("units", "metric")
                    .build()

            return chain.proceed(request
                    .newBuilder()
                    .url(newUrl)
                    .build())
        }
        return chain.proceed(chain.request())
    }

}