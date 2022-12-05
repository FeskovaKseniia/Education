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
            application, key,
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
        private const val hey = "d3f0ba3e-1fb0-4205-a06d-b26c50975b9f"
        private const val key = "8ab8381b-c56f-44d1-afde-219039ef7d2c"
    }
}