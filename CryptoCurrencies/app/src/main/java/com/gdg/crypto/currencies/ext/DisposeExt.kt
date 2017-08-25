package com.gdg.crypto.currencies.ext

import io.reactivex.disposables.Disposable

/**
 * Created by vkirillov on 24.08.2017.
 */
fun Disposable?.safeDispose() {
    if (this != null && !isDisposed) dispose()
}