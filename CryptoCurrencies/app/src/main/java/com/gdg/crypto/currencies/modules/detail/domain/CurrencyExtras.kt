package com.gdg.crypto.currencies.modules.detail.domain

import android.os.Bundle

/**
 * Created by vkirillov on 24.08.2017.
 */
class CurrencyExtras(val currency: String?, val coinName: String?) {

    fun toBundle() = Bundle().apply {
        putString(bundleCurrencyKey, currency)
        putString(bundleCoinNameKey, coinName)
    }

    companion object {
        val bundleCurrencyKey = "currency"
        val bundleCoinNameKey = "coin_name"

        fun fromArgs(args: Bundle?): CurrencyExtras {
            return args?.let {
                CurrencyExtras(it.getString(bundleCurrencyKey), it.getString(bundleCoinNameKey))
            } ?: CurrencyExtras(null, null)
        }
    }
}