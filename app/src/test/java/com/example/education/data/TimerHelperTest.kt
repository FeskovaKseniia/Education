package com.example.education.data

import com.example.education.utils.TimerHelper
import io.reactivex.rxjava3.schedulers.Timed
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.*


internal class TimerHelperTest {
    private val timer: TimerHelper = TimerHelper()

    @Test
    fun testMapping() {
        val expected = "1000"
        assertEquals(expected, timer.timerMapper(timed = Timed(1000L,1000L, TimeUnit.MILLISECONDS)))
    }
}