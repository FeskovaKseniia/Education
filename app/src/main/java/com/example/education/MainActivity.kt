package com.example.education

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
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
        if (intent.hasExtra("pushnotification")) {
            val notif = intent.extras?.getString("pushnotification")
            when (notif) {
                resources.getString(Notification.FLOW.stringId) -> navigateToFlow()
                resources.getString(Notification.LIST.stringId) -> navigateToList()
            }
        }
    }

    fun navigateToFlow() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.flowFragment)
    }

    fun navigateToList() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.listFragment)
    }

    companion object {
        private const val app = "cccc7fe0-6c00-4138-8106-6df7a5652e1d"
    }
}

enum class Notification(val stringId: Int) {
    FLOW(R.string.flow),
    LIST(R.string.list),
}