package com.example.mydairy

import android.app.Application
import common.di.AppComponent
import common.di.DaggerAppComponent
import common.di.DaggerComponentProvider

class ApplicationLoader() : Application(), DaggerComponentProvider {

    companion object {
        lateinit var shared: ApplicationLoader private set
    }

    override val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this, applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        shared = this

    }

}