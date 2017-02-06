package com.jcs.suadeome.generators

interface Random {
    fun random(range:IntRange): Int
}

class RealRandom : Random {

    companion object {
        val seed = System.nanoTime()
        val random = java.util.Random(seed)
    }

    override fun random(range: IntRange): Int {
        val quantity = range.last - range.first + 1
        val result = random.nextInt(quantity)  + range.first

        return result
    }

}
