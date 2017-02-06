package com.jcs.suadeome.generators

import com.jcs.suadeome.types.Id
import java.time.format.DateTimeFormatter

class IdGenerator(private val clock: Clock, val random: Random) {

    fun generate(type: EntityType): Id {
        val timestamp = clock.utcNow().format( DateTimeFormatter.ofPattern("yyDDDHHmmssSSS") )
        val random = random.random(100..999)
        return Id(type.idPrefix + timestamp + random)
    }

    companion object {
        fun default(): IdGenerator = IdGenerator(RealClock(), RealRandom() )
    }


}




