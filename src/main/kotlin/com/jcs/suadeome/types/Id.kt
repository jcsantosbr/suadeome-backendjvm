package com.jcs.suadeome.types

import java.math.BigDecimal

data class Id(private val _value: String) {
    val value = BigDecimal(_value)
}
