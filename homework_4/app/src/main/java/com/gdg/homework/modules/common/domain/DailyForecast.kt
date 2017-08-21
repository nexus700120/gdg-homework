package com.gdg.homework.modules.common.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by vkirillov on 21.08.2017.
 */
class WeatherForecast {

    @SerializedName("list") val list: List<DailyForecast>? = null

    class DailyForecast {

        @SerializedName("dt") val timestamp: Long? = null
        @SerializedName("temp") val temp: Temp? = null

        class Temp {
            @SerializedName("morn") val morn: Float? = null
            @SerializedName("eve") val eve: Float? = null
            @SerializedName("night") val night: Float? = null
            @SerializedName("day") val day: Float? = null
        }
    }
}