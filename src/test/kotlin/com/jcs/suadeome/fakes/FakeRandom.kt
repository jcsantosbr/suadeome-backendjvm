package com.jcs.suadeome.fakes

import com.jcs.suadeome.generators.Random

class FakeRandom(val value:Int): Random {
    override fun random(range: IntRange): Int = value

}
