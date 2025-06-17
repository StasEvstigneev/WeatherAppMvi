package com.esy.weatherappmvi

import android.app.Application
import com.esy.weatherappmvi.di.ApplicationComponent
import com.esy.weatherappmvi.di.DaggerApplicationComponent

class WeatherApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}