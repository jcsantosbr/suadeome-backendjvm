package com.jcs.suadeome.generators

import com.jcs.suadeome.fakes.FakeClock
import com.jcs.suadeome.fakes.FakeRandom
import com.jcs.suadeome.types.Id
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Test
import java.time.LocalDateTime

class IdGeneratorTest {

    private val fakeRandom = FakeRandom(888)
    private val fakeClock = FakeClock(LocalDateTime.of(2001, 1, 10, 21, 43, 37))
    private val generator = IdGenerator(fakeClock, fakeRandom)

    @Test
    fun generate() {
        val expectedId = Id(EntityType.PROFESSIONAL.idPrefix + "01010214337000888")

        val actualId = generator.generate(EntityType.PROFESSIONAL)

        assertThat(actualId, CoreMatchers.equalTo(expectedId))
    }

}
