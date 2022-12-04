package com.example.education.data

import android.util.Log
import io.reactivex.rxjava3.schedulers.Timed

class TimerHelper {
    fun timerMapper(timed: Timed<Long>): String {
        val time = timed.value().toString()
//        Log.d("TIMER", time)
        return time
    }
}