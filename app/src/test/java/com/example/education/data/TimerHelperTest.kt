package com.example.education.data

import io.reactivex.rxjava3.schedulers.Timed
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.*


internal class TimerHelperTest {
    private val timer: TimerHelper = TimerHelper()

    @Test
    fun testMapping() {
        val expected = "1500"
        assertEquals(expected, timer.timerMapper(timed = Timed(1000L,1000L, TimeUnit.MILLISECONDS)))
    }
}