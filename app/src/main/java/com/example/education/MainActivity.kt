package com.example.education

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.education.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<ListFragment>(R.id.fragment)
        }
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            if (!TextUtils.isEmpty(token)) {
                Log.d(TAG, "retrieve token successful : $token")
            } else {
                Log.w(TAG, "token should not be null...")
            }
        }.addOnFailureListener { e -> }
            .addOnCanceledListener {}.addOnCompleteListener { task ->
                Log.v(TAG, "This is the token : " + task.result)
            }

    }
    companion object {
        private const val TAG = "MainActivity"
    }
}