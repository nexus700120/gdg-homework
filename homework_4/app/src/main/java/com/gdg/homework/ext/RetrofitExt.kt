package com.gdg.homework.ext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by vkirillov on 21.08.2017.
 */

fun <T> Call<T>.enqueue(success: (response: T) -> Unit,
                        failure: (t: Throwable) -> Unit): Call<T> {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            response.body()?.let { success(it) } ?: failure(NullPointerException("body == null"))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (call.isCanceled) return
            failure(t)
        }
    })
    return this
}