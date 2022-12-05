package com.example.education

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.education.databinding.ActivityMainBinding
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCenter.start(
            application, another,
            Analytics::class.java, Crashes::class.java
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<ListFragment>(R.id.fragment)
        }
    }

    companion object {
        private const val key_option = "13b15121-cda2-4b9b-8fa6-3ec07b476085"
        private const val hey = "d3f0ba3e-1fb0-4205-a06d-b26c50975b9f"
        private const val key = "8ab8381b-c56f-44d1-afde-219039ef7d2c"
        private const val another = "3949eefa-4baf-4d4b-974d-3698dbf834ed"

    }
}