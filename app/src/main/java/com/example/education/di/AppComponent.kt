package com.example.education.di

import android.content.Context
import com.example.education.ListFragment
import com.example.education.MainActivity
import com.example.education.di.modules.ApiModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: ListFragment)
}
