package com.github.adriankuta.unbounddragdrop

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
}