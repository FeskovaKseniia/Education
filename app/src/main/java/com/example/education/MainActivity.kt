package com.example.education

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.education.databinding.ActivityMainBinding
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCenter.start(
            application, app,
            Analytics::class.java, Crashes::class.java
        )
    }

    companion object {
        private const val app = "cccc7fe0-6c00-4138-8106-6df7a5652e1d"
    }
}