package com.example.education

import android.app.Application
import com.example.education.di.AppComponent
import com.example.education.di.DaggerAppComponent

open class MyApp:Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}