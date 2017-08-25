package com.gdg.crypto.currencies.modules.detail.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by vkirillov on 24.08.2017.
 */
class HistoryResponse {
    @SerializedName("Data") val data: List<HistoryItem>? = null
}

class HistoryItem {
    @SerializedName("time") val time: Long? = null
    @SerializedName("close") val close: Float? = null
}