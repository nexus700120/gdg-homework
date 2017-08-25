package com.gdg.crypto.currencies.modules.common.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by vkirillov on 24.08.2017.
 */
class CurrencyListResponse {
    @SerializedName("BaseImageUrl") val baseImageUrl: String? = null
    @SerializedName("Data") val currencies: Map<String, CurrencyInfo>? = null
}

class CurrencyInfo {
    @SerializedName("Name") val name: String? = null
    @SerializedName("CoinName") val coinName: String? = null
    @SerializedName("ImageUrl") var imageUrl: String? = null
    @SerializedName("SortOrder") val sortOrder: String? = null
}
