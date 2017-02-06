package com.jcs.suadeome.clock

import com.jcs.suadeome.generators.RealClock
import org.junit.Assert.assertNotNull
import org.junit.Test

class ClockTest {

    private val clock = RealClock()

    @Test
    fun shouldReturnTheCurrentDateTimeInUTC() {

        val utcNow = clock.utcNow()
        println(utcNow)

        //TODO: maybe think a real way to test clocks, maybe!
        assertNotNull(utcNow);

    }

}
