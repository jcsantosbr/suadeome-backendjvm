package com.jcs.suadeome.generators

import java.time.LocalDateTime
import java.time.ZoneOffset


interface Clock {
    fun utcNow(): LocalDateTime
}

class RealClock : Clock {
    override fun utcNow(): LocalDateTime {
        return LocalDateTime.now(ZoneOffset.UTC)
    }
}
