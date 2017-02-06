package com.jcs.suadeome.fakes

import com.jcs.suadeome.generators.Clock
import java.time.LocalDateTime

class FakeClock(val time: LocalDateTime) : Clock {
    override fun utcNow(): LocalDateTime = time
}
