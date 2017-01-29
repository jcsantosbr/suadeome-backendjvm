package com.jcs.suadeome.types

import java.math.BigDecimal

class Id private constructor(private val value: String) {

    fun getValue(): BigDecimal {
        return BigDecimal(value)
    }

    companion object {

        fun id(value: String): Id {
            return Id(value)
        }
    }

}
