package com.jcs.suadeome.generators

import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Test

class RealRandomTest {


    @Test
    fun shouldGenerateAllNumbersInRangeIfRunEnoughTimes() {
        val allValues = linkedMapOf(10 to false, 11 to false, 12 to false)
        val random = RealRandom()

        for (i in 1..100) {
            val result = random.random(10..12)
            allValues.put(result, true)
        }

        val expectedValues = listOf(true, true, true)
        val actualValues = allValues.values.toList()

        assertThat(actualValues, CoreMatchers.equalTo(expectedValues))
    }

}
