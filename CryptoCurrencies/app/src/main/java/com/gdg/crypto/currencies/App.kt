package com.gdg.crypto.currencies

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

/**
 * Created by Vitaly Kiriilov on 26.08.2017.
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(applicationContext, Crashlytics())
    }
}